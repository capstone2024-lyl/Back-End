package capstone.capstone2024.domain.youtube.application;

import capstone.capstone2024.domain.user.domain.User;
import capstone.capstone2024.domain.user.domain.UserRepository;
import capstone.capstone2024.domain.youtube.domain.*;
import capstone.capstone2024.domain.youtube.dto.request.YoutubeChannelCreateRequestDto;
import capstone.capstone2024.domain.youtube.dto.response.YoutubeSubscribeCategoryResponseDto;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static capstone.capstone2024.global.error.ErrorCode.ROW_DOES_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class YoutubeService {
    private final UserRepository userRepository;
    private final YoutubeSubscribeRepository youtubeSubscribeRepository;
    private final YoutubeChannelRepository youtubeChannelRepository;

    @Transactional
    public List<YoutubeSubscribeResponseDto> getSubscriptions(String googleAccessToken, String loginId) {
        try {
            User user = userRepository.findByLoginId(loginId)
                    .orElseThrow(() -> new BadRequestException(ROW_DOES_NOT_EXIST, "존재하지 않는 아이디입니다."));

            YouTube youtube = new YouTube.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), request -> {
                request.getHeaders().setAuthorization("Bearer " + googleAccessToken);
            }).setApplicationName("youtube-subscriptions").build();

            YouTube.Subscriptions.List request = youtube.subscriptions().list("snippet");
            request.setMine(true);
            request.setMaxResults(10L);
            request.setRequestHeaders(new HttpHeaders().setCacheControl("no-cache"));
            request.setOauthToken(googleAccessToken);

            SubscriptionListResponse response = request.execute();
            List<Subscription> subscriptions = response.getItems();

            List<YoutubeSubscribeResponseDto> youtubeDtos = subscriptions.stream()
                    .map(subscription -> {
                        String channelName = subscription.getSnippet().getTitle();
                        YoutubeCategory category = youtubeChannelRepository.findByChannelName(channelName)
                                .map(YoutubeChannel::getCategory)
                                .orElse(YoutubeCategory.OTHERS);

                        return YoutubeSubscribeResponseDto.builder()
                                .channelName(channelName)
                                .category(category)
                                .build();
                    })
                    .collect(Collectors.toList());


            List<YoutubeSubscribe> youtubeEntities = youtubeDtos.stream()
                    .map(dto -> YoutubeSubscribe.builder()
                            .channelName(dto.getChannelName())
                            .category(dto.getCategory())
                            .user(user)
                            .build())
                    .collect(Collectors.toList());

            youtubeSubscribeRepository.saveAll(youtubeEntities);


            return youtubeDtos;

        } catch (GeneralSecurityException | IOException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
            throw new RuntimeException("Failed to retrieve subscriptions", e);
        }
    }


    @Transactional
    public String createYoutubeChannel(YoutubeCategory category, List<YoutubeChannelCreateRequestDto> youtubeChannelCreateRequestDtoList){
        List<YoutubeChannel> youtubeChannels = youtubeChannelCreateRequestDtoList.stream()
                .map(dto -> YoutubeChannel.builder()
                        .channelName(dto.getChannelName())
                        .category(category)
                        .build())
                .collect(Collectors.toList());

        youtubeChannelRepository.saveAll(youtubeChannels);
        return "ok";
    }




    public List<YoutubeSubscribeCategoryResponseDto> findCategory(String loginId){
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BadRequestException(ROW_DOES_NOT_EXIST, "존재하지 않는 아이디입니다."));

        List<YoutubeSubscribe> userSubscriptions = user.getYoutubeList();

        // 모든 구독 카테고리 가져오기
        List<YoutubeCategory> allCategories = userSubscriptions.stream()
                .map(YoutubeSubscribe::getCategory)
                .collect(Collectors.toList());

        // 카테고리별 등장 횟수 세기
        Map<YoutubeCategory, Long> categoryCountMap = allCategories.stream()
                .collect(Collectors.groupingBy(category -> category, Collectors.counting()));

        // "others" 카테고리 제외
        categoryCountMap.remove(YoutubeCategory.OTHERS);

        // 등장 횟수를 기준으로 내림차순 정렬
        List<Map.Entry<YoutubeCategory, Long>> sortedCategories =
                new ArrayList<>(categoryCountMap.entrySet());
        sortedCategories.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // 상위 3개 카테고리 선택
        List<YoutubeCategory> top3Categories = sortedCategories.stream()
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 결과 반환
        List<YoutubeSubscribeCategoryResponseDto> result = top3Categories.stream()
                .map(category -> YoutubeSubscribeCategoryResponseDto.builder()
                        .category(category)
                        .build())
                .collect(Collectors.toList());

        return result;

    }





}
