package capstone.capstone2024.domain.nickname.application;


import capstone.capstone2024.domain.user.domain.User;
import capstone.capstone2024.domain.nickname.domain.Nickname;
import capstone.capstone2024.domain.user.domain.UserRepository;
import capstone.capstone2024.domain.user.dto.response.UserNicknameResponseDto;
import capstone.capstone2024.global.error.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static capstone.capstone2024.global.error.ErrorCode.ROW_DOES_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class NicknameService {
    private final UserRepository userRepository;

    @Transactional
    public UserNicknameResponseDto addNickname(String loginId, Nickname nickname) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BadRequestException(ROW_DOES_NOT_EXIST, "존재하지 않는 사용자입니다."));

        if (!user.getNickname().contains(nickname)) {
            user.getNickname().add(nickname);
            userRepository.save(user);
        }

        return UserNicknameResponseDto.builder()
                .loginId(user.getLoginId())
                .nicknames(user.getNickname())
                .build();
    }

}
