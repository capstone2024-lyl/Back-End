package capstone.capstone2024.domain.chat.application;

import capstone.capstone2024.domain.chat.domain.Chat;
import capstone.capstone2024.domain.chat.domain.ChatRepository;
import capstone.capstone2024.domain.chat.domain.MBTI;
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

        List<String> userMessages = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                // 이름을 찾고 그 다음에 나오는 ':'를 기준으로 대화 내용 추출
                if (line.contains(user.getName() + " : ")) {
                    String[] parts = line.split(user.getName() + " : ");
                    if (parts.length > 1) {
                        String message = parts[1].trim();
                        userMessages.add(message);
                        chatCount++;
                    }
                }
            }
            reader.close();

            if(chatCount == 0){
                throw new BadRequestException(ErrorCode.INVALID_FILE_UPLOADED, "유효한 대화 내용이 아니거나 유효한 사용자 명이 아닙니다.");
            }

            int startIdx = Math.max(chatCount - 2000, 0); //음수면 startIdx가 0
            List<String> recentMessages = userMessages.subList(startIdx, chatCount);
            chatCount -= startIdx; // 채팅 카운트 업데이트


            // 추출된 메시지를 번역
            String combinedMessages = String.join("\n", recentMessages);
            String translatedMessages = openAIService.translateText(combinedMessages);

            // 모델을 사용해 mbti 예측 및 저장
            ChatPredictResponseDto responseDto = updateMBTI(predictMBTI(translatedMessages), chatCount, user);
            MBTI mbti = giveMBTI(responseDto);



            Chat chat = chatRepository.findByUserId(user.getId())
                    .orElse(null);

            if (chat != null) {
                // 기존 Chat 객체가 있는 경우 업데이트
                chat.update(responseDto.getEnergy(), responseDto.getRecognition(), responseDto.getDecision(), responseDto.getLifeStyle(), mbti, true, responseDto.getChatCount());
            } else {
                // 새로운 Chat 객체를 생성하고 저장
                chat = Chat.builder()
                        .energy(responseDto.getEnergy())
                        .recognition(responseDto.getRecognition())
                        .decision(responseDto.getDecision())
                        .lifeStyle(responseDto.getLifeStyle())
                        .mbti(mbti)
                        .isChecked(true)
                        .chatCount(responseDto.getChatCount())
                        .user(user)
                        .build();
                chatRepository.save(chat);
            }

            return responseDto;

        } catch (IOException e) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER, "Failed to process file");
        }
    }

    @Transactional
    public ChatPredictResponseDto predictMBTI(String translatedMessages) {
        if (translatedMessages.isEmpty()) {
            throw new BadRequestException(ErrorCode.INVALID_PARAMETER, "No file uploaded");
        }

        RestTemplate restTemplate = new RestTemplate();
        File tempFile;
        try {
            tempFile = File.createTempFile("filltered_chat", ".txt");
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            writer.write(translatedMessages);
            writer.close();
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
            if (response.getStatusCode() == HttpStatus.OK) {
                String jsonResponse = response.getBody();
                ChatPredictResponseDto chatPredictResponseDto = adjustValues(objectMapper.readValue(jsonResponse, ChatPredictResponseDto.class));

                return chatPredictResponseDto;

            } else {
                throw new InternalServerException(ErrorCode.INTERNAL_SERVER, "Failed to get response from Fl");
            }
        } catch (Exception e) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER, "Failed to get response from Flask server");
        } finally {
            tempFile.delete(); // 임시 파일 삭제
        }
    }

    private ChatPredictResponseDto adjustValues(ChatPredictResponseDto responseDto) {
        if (responseDto.getEnergy() == 50) responseDto.setEnergy(responseDto.getEnergy() - 1);
        if (responseDto.getRecognition() == 50) responseDto.setRecognition(responseDto.getRecognition() - 1);
        if (responseDto.getDecision() == 50) responseDto.setDecision(responseDto.getDecision() - 1);
        if (responseDto.getLifeStyle() == 50) responseDto.setLifeStyle(responseDto.getLifeStyle() - 1);
        return responseDto;
    }

    private ChatPredictResponseDto updateMBTI(ChatPredictResponseDto responseDto, Integer chatCount, User user){
        if(user.getMbti() != null){
            Chat preChat = user.getMbti();
            Integer updateChatCount = preChat.getChatCount() + chatCount;
            Integer updateEnergy = calculateMBTI(preChat.getEnergy(), preChat.getChatCount(), responseDto.getEnergy(), chatCount);
            Integer updateRecognition = calculateMBTI(preChat.getRecognition(), preChat.getChatCount(), responseDto.getRecognition(), chatCount);
            Integer updateDecision = calculateMBTI(preChat.getDecision(), preChat.getChatCount(), responseDto.getDecision(), chatCount);
            Integer updateLifeStyle = calculateMBTI(preChat.getLifeStyle(), preChat.getChatCount(), responseDto.getLifeStyle(), chatCount);

            return ChatPredictResponseDto.builder()
                    .energy(updateEnergy)
                    .recognition(updateRecognition)
                    .decision(updateDecision)
                    .lifeStyle(updateLifeStyle)
                    .chatCount(updateChatCount)
                    .build();

        } else{
            return ChatPredictResponseDto.builder()
                    .energy(responseDto.getEnergy())
                    .recognition(responseDto.getRecognition())
                    .decision(responseDto.getDecision())
                    .lifeStyle(responseDto.getLifeStyle())
                    .chatCount(chatCount)
                    .build();
        }
    }
    private Integer calculateMBTI(int preValue, int preChatCount, int value, int chatCount){
        return (preValue * preChatCount + value * chatCount) / (preChatCount + chatCount);
    }

    private MBTI giveMBTI(ChatPredictResponseDto responseDto) {
        String mbti = "";

        mbti += (responseDto.getEnergy() < 50 ? "I" : "E");
        mbti += (responseDto.getRecognition() < 50 ? "N" : "S");
        mbti += (responseDto.getDecision() < 50 ? "F" : "T");
        mbti += (responseDto.getLifeStyle() < 50 ? "P" : "J");

        return MBTI.valueOf(mbti);
    }



    @Transactional
    public ChatResponseDto findMBTI(String loginId){
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BadRequestException(ROW_DOES_NOT_EXIST, "존재하지 않는 사용자입니다."));
        Chat chat = chatRepository.findByUserId(user.getId()).orElse(null);

        boolean isChecked = (chat != null); // chat이 존재하면 true, 없으면 false

        return ChatResponseDto.builder()
                .energy((chat != null) ? chat.getEnergy() : null)
                .recognition((chat != null) ? chat.getRecognition() : null)
                .decision((chat != null) ? chat.getDecision() : null)
                .lifeStyle((chat != null) ? chat.getLifeStyle() : null)
                .mbti((chat != null) ? chat.getMbti() : null)
                .isChecked(isChecked)
                .build();
    }



    //번역 테스트
    @Transactional
    public String translateChat(MultipartFile file, String loginId) {
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
                // 이름을 찾고 그 다음에 나오는 ':'를 기준으로 대화 내용 추출
                if (line.contains(user.getName() + " : ")) {
                    String message = line.split(user.getName() + " : ")[1].trim();
                    userMessages.add(message);
                    chatCount++;
                }
            }
            reader.close();

            if(chatCount == 0){
                throw new BadRequestException(ErrorCode.INVALID_FILE_UPLOADED, "유효한 대화 내용이 아니거나 유효한 사용자 명이 아닙니다.");
            }

            //1. 사용자 이름의 대화 필터링
            String combinedMessages = String.join("\n", userMessages);

            //2. openai api로 번역 후 반환
            return openAIService.translateText(combinedMessages);

        } catch (IOException e) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER, "Failed to process file");
        }
    }
}
