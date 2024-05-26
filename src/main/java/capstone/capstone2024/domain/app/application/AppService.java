package capstone.capstone2024.domain.app.application;

import capstone.capstone2024.domain.app.domain.App;
import capstone.capstone2024.domain.app.domain.AppCategory;
import capstone.capstone2024.domain.app.domain.AppRepository;
import capstone.capstone2024.domain.app.dto.request.AppUsageCreateRequestDto;
import capstone.capstone2024.domain.app.dto.response.AppResponseDto;
import capstone.capstone2024.domain.app.dto.response.AppsResponseDto;
import capstone.capstone2024.domain.nickname.application.NicknameService;
import capstone.capstone2024.domain.user.domain.User;
import capstone.capstone2024.domain.nickname.domain.Nickname;
import capstone.capstone2024.domain.user.domain.UserRepository;
import capstone.capstone2024.global.error.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static capstone.capstone2024.global.error.ErrorCode.ROW_DOES_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class AppService {
    private final AppRepository appRepository;
    private final UserRepository userRepository;
    private final NicknameService nicknameService;


    @Transactional(readOnly = true)
    public AppsResponseDto findTop10App(String loginId){
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BadRequestException(ROW_DOES_NOT_EXIST, "존재하지 않는 사용자입니다."));

        List<App> apps = appRepository.findByUserIdOrderByUsageTimeDesc(user.getId());


        boolean isChecked = !apps.isEmpty();

        List<AppResponseDto> appsResponseDto= apps.stream()
                .map(app -> AppResponseDto.builder()
                        .appPackageName(app.getAppPackageName())
                        .usageTime(app.getUsageTime())
                        .build())
                .collect(Collectors.toList());



        return AppsResponseDto.builder()
                .apps(appsResponseDto)
                .isChecked(isChecked)
                .build();

    }

    @Transactional
    public String createAppUsage(String loginId, List<AppUsageCreateRequestDto> appUsageCreateRequestDto){
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BadRequestException(ROW_DOES_NOT_EXIST, "존재하지 않는 사용자입니다."));


        List<App> existingApps = appRepository.findByUserId(user.getId());
        Map<String, App> existingAppMap = existingApps.stream()
                .collect(Collectors.toMap(App::getAppPackageName, app -> app));

        List<App> appsToSave = appUsageCreateRequestDto.stream()
                .map(dto -> {
                    String packageName = dto.getAppPackageName();

                    if (existingAppMap.containsKey(packageName)) {
                        App exisitingApp = existingAppMap.get(packageName);
                        exisitingApp.updateUsageTime(dto.getUsageTime());
                        return exisitingApp;
                    } else {
                        AppCategory appCategory = AppCategory.fromPackageName(packageName);
                        return dto.toEntity(user, appCategory);
                    }
                })
                .collect(Collectors.toList());

        // 변환된 App 엔티티 리스트를 저장
        appRepository.saveAll(appsToSave);

        analyzeAppUsageAndAssignNickname(user);



        return "ok";
    }

    private void analyzeAppUsageAndAssignNickname(User user) {
        Map<AppCategory, Integer> usageByCategory = new HashMap<>();
        int totalUsageTime = 0;

        // 사용자의 앱 사용시간 집계
        List<App> userApps = appRepository.findByUserId(user.getId());
        for (App app : userApps) {
            int usageTime = app.getUsageTime();
            usageByCategory.merge(app.getAppCategory(), usageTime, Integer::sum);
            totalUsageTime += usageTime;
        }

        // 평균 사용시간과 비교하여 닉네임 부여
        for (AppCategory category : AppCategory.values()) {
            double averageUsage = category.getAverageUsage();
            if (usageByCategory.getOrDefault(category, 0) > averageUsage) {
                Nickname nickname = assignNicknameByCategory(category);
                if(nickname != null){
                    nicknameService.addNickname(user.getLoginId(), nickname);
                }
            }
        }

        //전체 사용시간으로 칭호 부여
        int totalAverageUsage = 2310;
        int totalMinimumUsage = 420;
        if(totalUsageTime > totalAverageUsage) {
            nicknameService.addNickname(user.getLoginId(), Nickname.ADDICT);
        } else if (totalUsageTime > totalMinimumUsage) {
            nicknameService.addNickname(user.getLoginId(), Nickname.HEALTY_USER);
        } else{
            nicknameService.addNickname(user.getLoginId(), Nickname.DOPAMINE_DETOXER);
        }
    }

    private Nickname assignNicknameByCategory(AppCategory category) {
        switch (category) {
            case CHAT:
                return Nickname.HEAVY_TALKER;
            case SNS:
                return Nickname.SNS_ADDICT;
            case GAME:
                return Nickname.GAME_HOLIC;
            case MUSIC:
                return Nickname.MUSIC_LOVER;
            case NEWS:
                return Nickname.NEWS_HUNTER;
            case VIDEO:
                return Nickname.VIDEO_ADDICT;
            case WEBTOON:
                return Nickname.WEBTOON_ADDICT;
            case E_BOOK:
                return Nickname.STUDY_MASTER;
            default:
                return null;
        }
    }


}
