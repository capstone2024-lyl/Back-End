package capstone.capstone2024.domain.user.controller;

import capstone.capstone2024.domain.user.application.UserService;
import capstone.capstone2024.domain.user.domain.User;
import capstone.capstone2024.domain.user.dto.request.UserCreateRequestDto;
import capstone.capstone2024.domain.user.dto.request.UserLogInRequestDto;
import capstone.capstone2024.domain.user.dto.response.UserLoginResponseDto;
import capstone.capstone2024.domain.user.dto.response.UserResponseDto;
import capstone.capstone2024.global.payload.ApiResponseTemplate;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public ApiResponseTemplate<UserResponseDto> signUp(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto){
        return ApiResponseTemplate.created(userService.signUp(userCreateRequestDto));
    }

    @GetMapping("/{loginId}/is-duplicated")
    public ApiResponseTemplate<String> isDuplicatedLoginId(@RequestParam String loginId){
        if(userService.checkLoginIdDuplicate(loginId)) return ApiResponseTemplate.ok("아이디가 중복됩니다.");
        return ApiResponseTemplate.ok("사용할 수 있는 아이디 입니다.");
    }

    @PostMapping("/login")
    public ApiResponseTemplate<UserLoginResponseDto> login(@Valid @RequestBody UserLogInRequestDto userLogInRequestDto){
        return  ApiResponseTemplate.ok(userService.login(userLogInRequestDto));
    }
}

