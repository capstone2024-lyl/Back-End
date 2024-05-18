package capstone.capstone2024.domain.youtube.application;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Subscription;
import com.google.api.services.youtube.model.SubscriptionListResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class YoutubeService {
    public List<Subscription> getSubscriptions(String accessToken) {
        try {
            YouTube youtube = new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), request -> {
                request.getHeaders().setAuthorization("Bearer " + accessToken);
            }).setApplicationName("youtube-subscriptions").build();

            YouTube.Subscriptions.List request = youtube.subscriptions().list("snippet,contentDetails");
            request.setMine(true);
            request.setMaxResults(10L);

            SubscriptionListResponse response = request.execute();
            return response.getItems();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Failed to retrieve subscriptions", e);
        }
    }
}
