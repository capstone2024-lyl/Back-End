package capstone.capstone2024.domain.app.application;

import capstone.capstone2024.domain.app.domain.App;
import capstone.capstone2024.domain.app.domain.AppCategory;
import capstone.capstone2024.domain.app.domain.AppRepository;
import capstone.capstone2024.domain.app.dto.request.AppUsageCreateRequestDto;
import capstone.capstone2024.domain.app.dto.response.AppResponseDto;
import capstone.capstone2024.domain.user.domain.User;
import capstone.capstone2024.domain.user.domain.UserRepository;
import capstone.capstone2024.global.error.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static capstone.capstone2024.global.error.ErrorCode.ROW_DOES_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class AppService {
    private final AppRepository appRepository;
    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public List<AppResponseDto> findTop10App(String loginId){
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BadRequestException(ROW_DOES_NOT_EXIST, "존재하지 않는 사용자입니다."));

        List<App> apps = appRepository.findByUserIdOrderByUsageTimeDesc(user.getId());

        return apps.stream()
                .map(app -> AppResponseDto.builder()
                        .appPackageName(app.getAppPackageName())
                        .usageTime(app.getUsageTime())
                        .build())
                .collect(Collectors.toList());

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
        return "ok";
    }


}
