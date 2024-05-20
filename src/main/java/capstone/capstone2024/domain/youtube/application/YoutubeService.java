package capstone.capstone2024.domain.youtube.application;

import capstone.capstone2024.domain.youtube.dto.YoutubeSubscribeResponseDto;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Subscription;
import com.google.api.services.youtube.model.SubscriptionListResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class YoutubeService {
    public List<YoutubeSubscribeResponseDto> getSubscriptions(String googleAccessToken) {
        try {
            YouTube youtube = new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), request -> {
                request.getHeaders().setAuthorization("Bearer " + googleAccessToken);
            }).setApplicationName("youtube-subscriptions").build();

            YouTube.Subscriptions.List request = youtube.subscriptions().list("snippet,contentDetails");
            request.setMine(true);
            request.setMaxResults(10L);
            request.setRequestHeaders(new HttpHeaders().setCacheControl("no-cache"));
            request.setOauthToken(googleAccessToken);



            SubscriptionListResponse response = request.execute();
            List<Subscription> subscriptions = response.getItems();

            return subscriptions.stream()
                    .map(subscription -> YoutubeSubscribeResponseDto.builder()
                            .id(subscription.getId())
                            .title(subscription.getSnippet().getTitle())
                            .description(subscription.getSnippet().getDescription())
                            .channelId(subscription.getSnippet().getResourceId().getChannelId())
                            .build())
                    .collect(Collectors.toList());
        } catch (GeneralSecurityException | IOException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
            throw new RuntimeException("Failed to retrieve subscriptions", e);
        }
    }
}
