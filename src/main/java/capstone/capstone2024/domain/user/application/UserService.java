package capstone.capstone2024.domain.user.application;

import capstone.capstone2024.domain.user.domain.User;
import capstone.capstone2024.domain.user.domain.UserRepository;
import capstone.capstone2024.domain.user.dto.request.UserCreateRequestDto;
import capstone.capstone2024.domain.user.dto.request.UserLogInRequestDto;
import capstone.capstone2024.domain.user.dto.response.UserLoginResponseDto;
import capstone.capstone2024.domain.user.dto.response.UserResponseDto;
import capstone.capstone2024.global.auth.JwtTokenUtil;
import capstone.capstone2024.global.error.exceptions.BadRequestException;
import capstone.capstone2024.global.error.exceptions.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static capstone.capstone2024.global.error.ErrorCode.INVALID_SIGNIN;
import static capstone.capstone2024.global.error.ErrorCode.ROW_DOES_NOT_EXIST;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${spring.jwt.secret}")
    private String secretKey;
    private Long expiredMs = 1000 * 60 * 60L;

    @Transactional(readOnly = true)
    public boolean checkLoginIdDuplicate(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    @Transactional
    public UserResponseDto signUp(UserCreateRequestDto userCreateRequestDto) {
        //아이디 중복 체크

        User user = userCreateRequestDto.toEntity(encoder.encode(userCreateRequestDto.getPassword()));
        userRepository.save(user);

        return UserResponseDto.builder()
                .loginId(user.getLoginId())
                .name(user.getName())
                .build();
    }


    @Transactional(readOnly = true)
    public UserLoginResponseDto login(UserLogInRequestDto userLogInRequestDto) {
        User user = userRepository.findByLoginId(userLogInRequestDto.getLoginId())
                .orElseThrow(() -> new BadRequestException(ROW_DOES_NOT_EXIST, "존재하지 않는 아이디입니다."));

        if(!encoder.matches(userLogInRequestDto.getPassword(), user.getPassword())) {
            throw new BadRequestException(ROW_DOES_NOT_EXIST, "비밀번호가 일치하지 않습니다.");
        }

        return UserLoginResponseDto.builder()
                .accessToken(JwtTokenUtil.createToken(user.getLoginId(), secretKey, expiredMs)).build();
    }

    @Transactional(readOnly = true)
    public User getLoginUserById(Long id) {
        if(id == null) return null;

        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }

    @Transactional(readOnly = true)
    public User getLoginUserByLoginId(String loginId) {
        if(loginId == null) return null;

        Optional<User> optionalUser = userRepository.findByLoginId(loginId);
        if(optionalUser.isEmpty()) return null;

        return optionalUser.get();
    }
}