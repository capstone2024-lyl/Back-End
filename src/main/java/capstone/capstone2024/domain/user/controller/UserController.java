package capstone.capstone2024.domain.user.controller;

import capstone.capstone2024.domain.user.application.UserService;
import capstone.capstone2024.domain.user.dto.request.UserCreateRequestDto;
import capstone.capstone2024.domain.user.dto.request.UserLogInRequestDto;
import capstone.capstone2024.domain.user.dto.response.UserLoginResponseDto;
import capstone.capstone2024.domain.user.dto.response.UserResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping(value = "/sign-up", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDto> signUp(
            @Valid @RequestPart("user") UserCreateRequestDto userCreateRequestDto,
            @RequestPart(name = "profileImage", required = false) MultipartFile profileImage
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.signUp(userCreateRequestDto, profileImage));
    }

    @GetMapping("/sign-up/is-duplicated")
    public ResponseEntity<String> isDuplicatedSignUpId(@RequestParam String loginId){
        if(userService.checkSignUpIdDuplicate(loginId)) return ResponseEntity.ok("can't use");
        return ResponseEntity.ok("can use");
    }

    @PutMapping(value = "/profileImage/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateProfileImage(
            @RequestParam(name = "profileImage", required = false) MultipartFile profileImage
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        userService.updateProfileImage(loginId, profileImage);
        return ResponseEntity.ok("profile image updated");
    }

    @PutMapping(value = "/profileImage/delete")
    public ResponseEntity<String> deleteProfileImage(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        userService.deleteProfileImage(loginId);
        return ResponseEntity.ok("profile image updated");
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



}

