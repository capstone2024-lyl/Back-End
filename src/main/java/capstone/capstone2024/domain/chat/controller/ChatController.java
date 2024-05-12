package capstone.capstone2024.domain.chat.controller;


import capstone.capstone2024.domain.chat.application.ChatService;

import capstone.capstone2024.domain.chat.dto.response.ChatResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
            @RequestParam("loginId") String loginId,
            @RequestParam("path") String path
    ) {
        String result = chatService.filterChat(file, loginId, path);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{loginId}")
    public ResponseEntity<ChatResponseDto> findMBTI(
            @PathVariable String loginId
    ){
        return ResponseEntity.ok(chatService.findMBTI(loginId));
    }




}
