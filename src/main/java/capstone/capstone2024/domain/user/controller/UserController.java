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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/login/is-duplicated")
    public ResponseEntity<String> isDuplicatedLoginId(@RequestParam String loginId){
        if(userService.checkLoginIdDuplicate(loginId)) return ResponseEntity.ok("can't use");
        return ResponseEntity.ok("can use");
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@Valid @RequestBody UserLogInRequestDto userLogInRequestDto){
        return  ResponseEntity.ok(userService.login(userLogInRequestDto));
    }

    @GetMapping("/{loginId}")
    public ResponseEntity<UserResponseDto> findUser(@PathVariable String loginId){
        return ResponseEntity.ok(userService.findUser(loginId));
    }




}

