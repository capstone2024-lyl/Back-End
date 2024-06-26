package capstone.capstone2024.domain.youtube.application;

import capstone.capstone2024.domain.nickname.application.NicknameService;
import capstone.capstone2024.domain.nickname.domain.Nickname;
import capstone.capstone2024.domain.user.domain.User;
import capstone.capstone2024.domain.user.domain.UserRepository;
import capstone.capstone2024.domain.youtube.domain.*;
import capstone.capstone2024.domain.youtube.dto.request.YoutubeChannelCreateRequestDto;
import capstone.capstone2024.domain.youtube.dto.response.YoutubeTop3CategoriesResponseDto;
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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static capstone.capstone2024.global.error.ErrorCode.ROW_DOES_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class YoutubeService {
    private final UserRepository userRepository;
    private final YoutubeChannelRepository youtubeChannelRepository;
    private final YoutubeCategoriesRepository youtubeCategoriesRepository;
    private final NicknameService nicknameService;

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
                        YoutubeCategory category = youtubeChannelRepository.findFirstByChannelName(channelName)
                                .map(YoutubeChannel::getCategory)
                                .orElse(YoutubeCategory.OTHERS);

                        return YoutubeSubscribeResponseDto.builder()
                                .channelName(channelName)
                                .category(category)
                                .build();
                    })
                    .collect(Collectors.toList());

            //카테고리 별 개수저장
            saveCategoryCounts(youtubeDtos, user);
            Nickname userNickname = assignNicknameByYoutube(user);
            nicknameService.addNickname(loginId, userNickname);

            return youtubeDtos;

        } catch (GeneralSecurityException | IOException e) {
            System.out.println("e.getMessage() = " + e.getMessage());
            throw new RuntimeException("Failed to retrieve subscriptions", e);
        }
    }

    private void saveCategoryCounts(List<YoutubeSubscribeResponseDto> youtubeDtos, User user) {
        List<YoutubeCategories> existCategories = youtubeCategoriesRepository.findByUserIdAndIsDeletedFalse(user.getId());

        existCategories.forEach(category -> category.setIsDeleted(true));

        // 새로운 카테고리 엔티티들을 저장
        Map<YoutubeCategory, Long> categoryCountMap = youtubeDtos.stream()
                .filter(dto -> dto.getCategory() != YoutubeCategory.OTHERS)
                .collect(Collectors.groupingBy(YoutubeSubscribeResponseDto::getCategory, Collectors.counting()));

        List<YoutubeCategories> newCategories = categoryCountMap.entrySet().stream()
                .map(entry -> YoutubeCategories.builder()
                        .user(user)
                        .category(entry.getKey())
                        .categoryCount(entry.getValue()) // Long을 int로 변환하여 저장
                        .isDeleted(false)
                        .build())
                .collect(Collectors.toList());

        youtubeCategoriesRepository.saveAll(newCategories);
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



    @Transactional(readOnly = true)
    public YoutubeTop3CategoriesResponseDto findTop3Categories(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BadRequestException(ROW_DOES_NOT_EXIST, "존재하지 않는 아이디입니다."));

        // 카테고리별로 카운트가 저장된 youtubecategories 엔티티 조회
        List<YoutubeCategories> categories = youtubeCategoriesRepository.findByUserIdAndIsDeletedFalse(user.getId());

        // 카테고리별 카운트로부터 Top 3 카테고리를 내림차순으로 추출
        List<YoutubeCategory> top3Categories = categories.stream()
                .sorted(Comparator.comparingLong(YoutubeCategories::getCategoryCount).reversed()) // 내림차순 정렬
                .limit(3) // 상위 3개만 가져오기
                .map(YoutubeCategories::getCategory)
                .collect(Collectors.toList());

        return YoutubeTop3CategoriesResponseDto.builder()
                .youtubeCategoryList(top3Categories)
                .isChecked(!top3Categories.isEmpty())
                .build();
    }


    private Nickname assignNicknameByYoutube(User user){
        List<YoutubeCategories> categories = youtubeCategoriesRepository.findByUserIdAndIsDeletedFalse(user.getId());
        if (categories.isEmpty()) {
            return null;
        }

        YoutubeCategory topCategory = categories.stream()
                .sorted(Comparator.comparingLong(YoutubeCategories::getCategoryCount).reversed())
                .map(YoutubeCategories::getCategory)
                .findFirst()
                .orElse(YoutubeCategory.OTHERS);

        return switch (topCategory) {
            case ENTERTAINMENT -> Nickname.ENTERTAINMENT_PD;
            case GAME -> Nickname.GAME_HOLIC;
            case IT -> Nickname.TECH_MASTER;
            case SPORTS -> Nickname.ATLETE_AT_HEART;
            case MOVIE -> Nickname.COUCH_DIRECTOR;
            case EDUCATION -> Nickname.KNOWLEDGE_SEEKER;
            case SCIENCE -> Nickname.SCIENCE_GEEK;
            case TRAVEL -> Nickname.TRAVELER;
            case COMEDY -> Nickname.COMEDY_LOVER;
            case PET -> Nickname.PET_LOVER;
            case NEWS -> Nickname.NEWS_JUNKIE;
            default -> null;
        };

    }

}
