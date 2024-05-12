package capstone.capstone2024.domain.user.application;

//import capstone.capstone2024.domain.app.domain.AppRepository;
//import capstone.capstone2024.domain.category.domain.CategoryRepository;
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

import static capstone.capstone2024.global.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserService {
//    private final AppRepository appRepository;
    private final UserRepository userRepository;
//    private final CategoryRepository categoryRepository;
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
        if(userRepository.existsByLoginId(userCreateRequestDto.getLoginId())){
            throw new BadRequestException(ROW_ALREADY_EXIST, "이미 존재하는 아이디입니다. 중복을 확인해주세요.");
        }

        User user = userCreateRequestDto.toEntity(encoder.encode(userCreateRequestDto.getPassword()));
        userRepository.save(user);

        return UserResponseDto.builder()
                .loginId(user.getLoginId())
                .name(user.getName())
                .birthday(user.getBirthday())
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
        if(loginId == null){
            new BadRequestException(NEED_SIGN_IN, "로그인 후 사용가능합니다.");
        }
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BadRequestException(ROW_DOES_NOT_EXIST, "존재하지 않는 사용자입니다."));


        return user;
    }

    @Transactional(readOnly = true)
    public UserResponseDto findUser(String loginId){
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BadRequestException(ROW_DOES_NOT_EXIST, "존재하지 않는 사용자입니다."));

        return UserResponseDto.builder()
                .name(user.getName())
                .birthday(user.getBirthday())
//                .mbti(user.getMbti())
//                .app(appRepository.findTop3ByUserIdOrderByUsageTimeDesc(user.getId()))
//                .category(categoryRepository.findTop3ByUserIdOrderByPlayCountDesc(user.getId()))
                .nickname(user.getNickname())
                .build();
    }




}