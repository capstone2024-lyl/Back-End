package capstone.capstone2024.domain.openai.controller;

import capstone.capstone2024.domain.app.dto.request.AppUsageCreateRequestDto;
import capstone.capstone2024.domain.openai.application.OpenAIService;
import capstone.capstone2024.domain.youtube.dto.response.YoutubeSubscribeResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OpenAIController {

    private final OpenAIService openAIService;

    @GetMapping("/api/v1/youtube/search")
    public ResponseEntity<String> youtubeSearch(@RequestParam String channelNames) {
//        List<YoutubeSubscribeResponseDto> result = openAIService.youtubeSearch(channelNames);
        return ResponseEntity.ok(openAIService.youtubeSearch(channelNames));
    }

}