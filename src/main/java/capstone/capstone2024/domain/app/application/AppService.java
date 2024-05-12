package capstone.capstone2024.domain.app.application;

import capstone.capstone2024.domain.app.domain.App;
import capstone.capstone2024.domain.app.domain.AppRepository;
import capstone.capstone2024.domain.app.dto.response.AppResponseDto;
import capstone.capstone2024.domain.user.domain.User;
import capstone.capstone2024.domain.user.domain.UserRepository;
import capstone.capstone2024.global.error.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

import static capstone.capstone2024.global.error.ErrorCode.ROW_DOES_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class AppService {
    private final AppRepository appRepository;
    private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public List<AppResponseDto> findTop3App(String loginId){
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BadRequestException(ROW_DOES_NOT_EXIST, "존재하지 않는 사용자입니다."));

        Pageable topThree = PageRequest.of(0, 3);
        Page<App> apps = appRepository.findByUserIdOrderByUsageTimeDesc(user, topThree);

        return apps.getContent()
                .stream()
                .map(app -> AppResponseDto.builder()
                        .appName(app.getAppName())
                        .usageTime(app.getUsageTime())
                        .build())
                .collect(Collectors.toList());

    }

}
