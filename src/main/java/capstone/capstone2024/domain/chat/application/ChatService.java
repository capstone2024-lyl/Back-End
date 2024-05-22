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
    public String filterChat(MultipartFile file, String loginId, String path) {
        if (file.isEmpty()) {
            throw new BadRequestException(ErrorCode.NO_FILE_UPLOADED, "NO file uploaded");
        }
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BadRequestException(ROW_DOES_NOT_EXIST, "존재하지 않는 사용자입니다."));


        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String outputFileName = user.getName() + "_" + file.getOriginalFilename();
            Path outputPath = Paths.get(path + outputFileName);

            List<String> userMessages = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 2 && parts[1].replaceAll("\"", "").equals(user.getName())) {
                    userMessages.add(parts[2].replaceAll("\"", "").trim());
                }
            }

            reader.close();

            String combinedMessages = String.join("\n", userMessages);
            String translatedMessages = openAIService.translateText(combinedMessages);


            BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath.toFile()));
            writer.write(translatedMessages);
            writer.close();


            return "Filtered file created successfully: " + outputFileName;


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
