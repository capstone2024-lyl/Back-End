package capstone.capstone2024.domain.youtube.application;

import capstone.capstone2024.domain.openai.application.OpenAIService;
import capstone.capstone2024.domain.user.domain.User;
import capstone.capstone2024.domain.user.domain.UserRepository;
import capstone.capstone2024.domain.youtube.domain.Youtube;
import capstone.capstone2024.domain.youtube.domain.YoutubeRepository;
import capstone.capstone2024.domain.youtube.dto.response.YoutubeSubscribeResponseDto;
import capstone.capstone2024.global.error.exceptions.BadRequestException;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Subscription;
import com.google.api.services.youtube.model.SubscriptionListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.stream.Collectors;

import static capstone.capstone2024.global.error.ErrorCode.ROW_DOES_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class YoutubeService {
    private final UserRepository userRepository;
    private final YoutubeRepository youtubeRepository;

    @Transactional
    public List<YoutubeSubscribeResponseDto> getSubscriptions(String googleAccessToken, String loginId) {
        try {
            User user = userRepository.findByLoginId(loginId)
                    .orElseThrow(() -> new BadRequestException(ROW_DOES_NOT_EXIST, "존재하지 않는 아이디입니다."));

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

//            // 구독 채널의 타이틀을 줄바꿈으로 구분하여 문자열로 생성
//            String channelTitles = subscriptions.stream()
//                    .map(subscription -> subscription.getSnippet().getTitle())
//                    .collect(Collectors.joining("\n"));
////            createChannelCategory(channelTitles);


            List<Youtube> youtubeEntities = subscriptions.stream()
                    .map(subscription -> Youtube.builder()
                            .channelName(subscription.getSnippet().getTitle())
                            .user(user)
                            .build())
                    .collect(Collectors.toList());

            youtubeRepository.saveAll(youtubeEntities);


            return subscriptions.stream()
                    .map(subscription -> YoutubeSubscribeResponseDto.builder()
                            .title(subscription.getSnippet().getTitle())
                            .build())
                    .collect(Collectors.toList());
        } catch (GeneralSecurityException | IOException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
            throw new RuntimeException("Failed to retrieve subscriptions", e);
        }
    }

//    public void createChannelCategory(String channelTitles){
//        String subscibeResponse = openAIService.youtubeSearch(channelTitles);
//
//    }

}
