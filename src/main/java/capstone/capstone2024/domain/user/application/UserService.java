package capstone.capstone2024.domain.user.application;

import capstone.capstone2024.domain.app.application.AppService;
import capstone.capstone2024.domain.app.dto.response.AppsResponseDto;
import capstone.capstone2024.domain.chat.application.ChatService;
import capstone.capstone2024.domain.chat.dto.response.ChatResponseDto;
import capstone.capstone2024.domain.storage.application.StorageService;
import capstone.capstone2024.domain.user.domain.User;
import capstone.capstone2024.domain.nickname.domain.Nickname;
import capstone.capstone2024.domain.user.domain.UserRepository;
import capstone.capstone2024.domain.user.dto.request.UserCreateRequestDto;
import capstone.capstone2024.domain.user.dto.request.UserLogInRequestDto;
import capstone.capstone2024.domain.user.dto.response.UserLoginResponseDto;
import capstone.capstone2024.domain.user.dto.response.UserResponseDto;
import capstone.capstone2024.domain.youtube.application.YoutubeService;
import capstone.capstone2024.domain.youtube.dto.response.YoutubeTop3CategoriesResponseDto;
import capstone.capstone2024.global.auth.JwtTokenUtil;
import capstone.capstone2024.global.error.exceptions.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static capstone.capstone2024.global.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final AppService appService;
    private final YoutubeService youtubeService;
    private final ChatService chatService;
    private final StorageService storageService;

    @Value("${spring.jwt.secret}")
    private String secretKey;
    private Long expiredMs = 1000 * 60 * 60L;

    @Value("${cloud.default.profile.image.url}")
    private String defaultProfileImageUrl;

    @Transactional(readOnly = true)
    public boolean checkSignUpIdDuplicate(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    @Transactional
    public UserResponseDto signUp(UserCreateRequestDto userCreateRequestDto) {
        //아이디 중복 체크
        if(userRepository.existsByLoginId(userCreateRequestDto.getLoginId())){
            throw new BadRequestException(ROW_ALREADY_EXIST, "이미 존재하는 아이디입니다. 중복을 확인해주세요.");
        }

        String imageUrl = defaultProfileImageUrl;
        MultipartFile profileImage = userCreateRequestDto.getProfileImage();
        if (profileImage != null && !profileImage.isEmpty()) {
            System.out.println("이미지를 받았어요");
            imageUrl = storageService.upload(profileImage);
        } else{
            System.out.println("이미지가 안와요 ㅠㅠ");
        }

        User user = userCreateRequestDto.toEntity(encoder.encode(userCreateRequestDto.getPassword()), imageUrl);
        userRepository.save(user);

        return UserResponseDto.builder()
                .loginId(user.getLoginId())
                .name(user.getName())
                .birthday(user.getBirthday())
                .build();
    }


    @Transactional
    public void updateProfileImage(String loginId, MultipartFile file) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BadRequestException(ROW_DOES_NOT_EXIST, "존재하지 않는 사용자입니다."));
        String imageUrl = storageService.upload(file);
        user.updateProfileImageUrl(imageUrl);
        userRepository.save(user); // 변경 감지를 통해 저장
    }

    @Transactional
    public void deleteProfileImage(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BadRequestException(ROW_DOES_NOT_EXIST, "존재하지 않는 사용자입니다."));

        String currentProfileImageUrl = user.getProfileImageUrl();

        if(!currentProfileImageUrl.equals(defaultProfileImageUrl)){
            storageService.deleteImageFromS3(user.getProfileImageUrl());
        }
        user.updateProfileImageUrl(defaultProfileImageUrl);
        userRepository.save(user); // 변경 감지를 통해 저장
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


        AppsResponseDto appsResponseDto = appService.findTop10App(loginId);
        YoutubeTop3CategoriesResponseDto top3Categories = youtubeService.findTop3Categories(loginId);
        ChatResponseDto mbtiInfo = chatService.findMBTI(loginId);

        List<String> userNicknameValues = user.getNickname().stream()
                .map(Nickname::getDescription)
                .collect(Collectors.toList());

        return UserResponseDto.builder()
                .name(user.getName())
                .birthday(user.getBirthday())
                .apps(appsResponseDto)
                .category(top3Categories)
                .mbti(mbtiInfo)
                .nicknames(userNicknameValues)
                .profileImageUrl(user.getProfileImageUrl())
                .build();
    }








}