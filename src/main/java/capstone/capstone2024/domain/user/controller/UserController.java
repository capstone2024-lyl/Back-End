package capstone.capstone2024.domain.user.controller;

import capstone.capstone2024.domain.user.application.UserService;
import capstone.capstone2024.domain.user.domain.UserNickname;
import capstone.capstone2024.domain.user.dto.request.UserCreateRequestDto;
import capstone.capstone2024.domain.user.dto.request.UserLogInRequestDto;
import capstone.capstone2024.domain.user.dto.response.UserLoginResponseDto;
import capstone.capstone2024.domain.user.dto.response.UserNicknameResponseDto;
import capstone.capstone2024.domain.user.dto.response.UserResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<UserResponseDto> signUp(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signUp(userCreateRequestDto));
    }

    @GetMapping("/sign-up/is-duplicated")
    public ResponseEntity<String> isDuplicatedSignUpId(@RequestParam String loginId){
        if(userService.checkSignUpIdDuplicate(loginId)) return ResponseEntity.ok("can't use");
        return ResponseEntity.ok("can use");
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@Valid @RequestBody UserLogInRequestDto userLogInRequestDto){
        return  ResponseEntity.ok(userService.login(userLogInRequestDto));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> findUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        return ResponseEntity.ok(userService.findUser(loginId));
    }

    @PostMapping("/nickname")
    public ResponseEntity<UserNicknameResponseDto> addNickname(@RequestParam UserNickname nickname) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        return ResponseEntity.ok(userService.addNickname(loginId, nickname));
    }




}

