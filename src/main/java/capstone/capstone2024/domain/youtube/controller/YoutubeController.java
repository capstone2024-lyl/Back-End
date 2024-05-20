package capstone.capstone2024.domain.youtube.controller;

import capstone.capstone2024.domain.youtube.application.YoutubeService;
import capstone.capstone2024.domain.oauth2.application.OAuth2Service;
import capstone.capstone2024.domain.youtube.dto.YoutubeSubscribeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/youtube")
public class YoutubeController {
    private final YoutubeService youtubeService;

    @GetMapping("/subscriptions")
    public ResponseEntity<List<YoutubeSubscribeResponseDto>> getSubscriptions(
            @RequestHeader("X-Google-Token") String googleToken
    ) {
        return ResponseEntity.ok(youtubeService.getSubscriptions(googleToken));
    }


}