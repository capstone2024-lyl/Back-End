package capstone.capstone2024.domain.chat.controller;


import capstone.capstone2024.domain.chat.application.ChatService;

import capstone.capstone2024.domain.chat.dto.response.ChatPredictResponseDto;
import capstone.capstone2024.domain.chat.dto.response.ChatResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {
    private final ChatService chatService;

    @PostMapping(value = "/predict-mbti", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ChatPredictResponseDto> fileUploadAndPredictMBTI(
            @RequestParam("file") MultipartFile file
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        return ResponseEntity.ok(chatService.uploadChat(file, loginId));
    }

    @PostMapping(value = "/translate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> fileUploadAndTranslate(
            @RequestParam("file") MultipartFile file
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        String translatedMessages = chatService.translateChat(file, loginId);
        return ResponseEntity.ok(translatedMessages);
    }


    @GetMapping("/findMBTI")
    public ResponseEntity<ChatResponseDto> findMBTI(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        return ResponseEntity.ok(chatService.findMBTI(loginId));
    }





}
