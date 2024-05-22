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

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {
    private final ChatService chatService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> fileUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("path") String path
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        return ResponseEntity.ok(chatService.filterChat(file, loginId, path));
    }

    @GetMapping("/findMBTI")
    public ResponseEntity<ChatResponseDto> findMBTI(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        return ResponseEntity.ok(chatService.findMBTI(loginId));
    }

    @PostMapping(value = "/predict_mbti", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ChatPredictResponseDto> predictMBTI(@RequestParam("file") MultipartFile file) {

        return ResponseEntity.ok(chatService.predictMBTI(file));
    }




}
