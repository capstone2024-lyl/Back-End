package capstone.capstone2024.domain.chat.application;

import capstone.capstone2024.domain.chat.domain.Chat;
import capstone.capstone2024.domain.chat.domain.ChatRepository;
import capstone.capstone2024.domain.chat.dto.response.ChatPredictResponseDto;
import capstone.capstone2024.domain.chat.dto.response.ChatResponseDto;
import capstone.capstone2024.domain.openai.application.OpenAIService;
import capstone.capstone2024.domain.user.domain.User;
import capstone.capstone2024.domain.user.domain.UserRepository;
import capstone.capstone2024.global.error.ErrorCode;
import capstone.capstone2024.global.error.exceptions.BadRequestException;
import capstone.capstone2024.global.error.exceptions.InternalServerException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static capstone.capstone2024.global.error.ErrorCode.ROW_DOES_NOT_EXIST;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final OpenAIService openAIService;
    private final ObjectMapper objectMapper;

    @Value("${spring.flask.api.url}")
    private String flaskApiUrl;

    @Transactional
    public ChatPredictResponseDto uploadChat(MultipartFile file, String loginId) {
        if (file.isEmpty()) {
            throw new BadRequestException(ErrorCode.NO_FILE_UPLOADED, "NO file uploaded");
        }
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BadRequestException(ROW_DOES_NOT_EXIST, "존재하지 않는 사용자입니다."));

        int chatCount = 0;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            List<String> userMessages = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 2 && parts[1].replaceAll("\"", "").equals(user.getName())) {
                    userMessages.add(parts[2].replaceAll("\"", "").trim());
                    chatCount++;
                }
            }
            reader.close();

            //1. 사용자 이름의 대화 필터링
            String combinedMessages = String.join("\n", userMessages);

            //2. openai api로 번역
            String translatedMessages = openAIService.translateText(combinedMessages);

            //3. 모델을 사용해 mbti 예측 및 저장
            ChatPredictResponseDto responseDto = updateMBTI(predictMBTI(translatedMessages), chatCount, user);
            MBTI mbti = giveMBTI(responseDto);

            //4. openai api에 mbti와 파일을 주고 근거 찾기
//            String analyzedMessages = openAIService.analyzeMBTI(combinedMessages, mbti.name());


            if(user.getMbti() != null){
                System.out.println("유저의 채팅정보가 존재합니다");
                user.getMbti().update(responseDto.getEnergy(), responseDto.getRecognition(), responseDto.getDecision(), responseDto.getLifeStyle(), mbti, true, responseDto.getChatCount());
            } else {
                Chat chat = Chat.builder()
                        .energy(responseDto.getEnergy())
                        .recognition(responseDto.getRecognition())
                        .decision(responseDto.getDecision())
                        .lifeStyle(responseDto.getLifeStyle())
                        .mbti(mbti)
                        .isChecked(true)
                        .chatCount(responseDto.getChatCount())
//                        .description(analyzedMessages)
                        .user(user)
                        .build();
                chatRepository.save(chat); // chat 엔티티 저장
            }

            return responseDto;

        } catch (IOException e) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER, "Failed to process file");
        }
    }


    @Transactional
    public ChatResponseDto findMBTI(String loginId){
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BadRequestException(ROW_DOES_NOT_EXIST, "존재하지 않는 사용자입니다."));

        Chat chat = chatRepository.findById(user.getChat().getId())
                .orElseThrow(() -> new BadRequestException(ROW_DOES_NOT_EXIST, "mbti 검사 결과가 없습니다."));


        return ChatResponseDto.builder()
                .energy(chat.getEnergy())
                .recognition(chat.getRecognition())
                .decision(chat.getDecision())
                .lifeStyle(chat.getLifeStyle())
                .mbti(chat.getMbti())
                .isChecked(true)
                .build();
    }

    @Transactional
    public ChatPredictResponseDto predictMBTI(MultipartFile file) {
        if (file.isEmpty()) {
            throw new InternalServerException(ErrorCode.NO_FILE_UPLOADED, "No file uploaded");
        }

        RestTemplate restTemplate = new RestTemplate();
        File tempFile = new File(System.getProperty("java.io.tmpdir"), file.getOriginalFilename());
        try {
            file.transferTo(tempFile);
        } catch (IOException e) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER, "Failed to save temp file");
        }

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(tempFile));


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    flaskApiUrl, HttpMethod.POST, requestEntity, String.class);
            log.debug("Response Entity: {}", response);
            if (response.getStatusCode() == HttpStatus.OK) {
                String jsonResponse = response.getBody();
                return objectMapper.readValue(jsonResponse, ChatPredictResponseDto.class);
            } else {
                throw new InternalServerException(ErrorCode.INTERNAL_SERVER, "Failed to get response from Fl");
            }
        } catch (Exception e) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER, "Failed to get response from Flask server");
        } finally {
            tempFile.delete(); // 임시 파일 삭제
        }
    }




}
