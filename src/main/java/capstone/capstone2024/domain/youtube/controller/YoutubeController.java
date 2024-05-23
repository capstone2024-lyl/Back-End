package capstone.capstone2024.domain.youtube.controller;

import capstone.capstone2024.domain.youtube.application.YoutubeService;
import capstone.capstone2024.domain.youtube.domain.YoutubeCategory;
import capstone.capstone2024.domain.youtube.dto.request.YoutubeChannelCreateRequestDto;
import capstone.capstone2024.domain.youtube.dto.response.YoutubeSubscribeResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        return ResponseEntity.ok(youtubeService.getSubscriptions(googleToken, loginId));
    }

    @PostMapping("/channels")
    public ResponseEntity<String> createChannels(
            @RequestParam YoutubeCategory category,
            @Valid @RequestBody List<YoutubeChannelCreateRequestDto> youtubeChannelCreateRequestDtos
    ){
        return ResponseEntity.ok(youtubeService.createYoutubeChannel(category, youtubeChannelCreateRequestDtos));

    }


}