package capstone.capstone2024.domain.youtube.controller;

import capstone.capstone2024.domain.youtube.application.YoutubeService;
import capstone.capstone2024.domain.oauth2.application.OAuth2Service;
import com.google.api.services.youtube.model.Subscription;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final OAuth2Service oAuth2Service;

    @GetMapping("/subscriptions")
    public ResponseEntity<List<Subscription>> getSubscriptions(
            @RequestHeader("X-Google-Token") String googleToken
    ) {
        oAuth2Service.verifyGoogleToken(googleToken);
        return ResponseEntity.ok(youtubeService.getSubscriptions(googleToken));
    }
}