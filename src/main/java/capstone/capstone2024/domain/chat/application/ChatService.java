package capstone.capstone2024.domain.chat.application;

import capstone.capstone2024.domain.chat.domain.Chat;
import capstone.capstone2024.domain.chat.domain.ChatRepository;
import capstone.capstone2024.domain.chat.dto.response.ChatResponseDto;
import capstone.capstone2024.domain.user.domain.User;
import capstone.capstone2024.domain.user.domain.UserRepository;
import capstone.capstone2024.global.error.ErrorCode;
import capstone.capstone2024.global.error.exceptions.BadRequestException;
import capstone.capstone2024.global.error.exceptions.InternalServerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import static capstone.capstone2024.global.error.ErrorCode.ROW_DOES_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    @Transactional
    public String filterChat(MultipartFile file, String loginId, String path) {
        if (file.isEmpty()) {
            throw new BadRequestException(ErrorCode.NO_FILE_UPLOADED, "NO file uploaded");
        }
        System.out.println("loginId = " + loginId);
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BadRequestException(ROW_DOES_NOT_EXIST, "존재하지 않는 사용자입니다."));

        System.out.println("user.getName() = " + user.getName());

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String outputFileName = user.getName() + "_" + file.getOriginalFilename();
            Path outputPath = Paths.get(path + outputFileName);

            BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath.toFile()));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 2 && parts[1].replaceAll("\"", "").equals(user.getName())) {
                    writer.write(parts[2].replaceAll("\"", "").trim() + "\n");
                }
            }

            writer.close();
            reader.close();

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



}
