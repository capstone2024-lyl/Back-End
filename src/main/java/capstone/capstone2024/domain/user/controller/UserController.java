package capstone.capstone2024.domain.user.controller;

import capstone.capstone2024.domain.user.application.UserService;
import capstone.capstone2024.domain.user.domain.User;
import capstone.capstone2024.domain.user.dto.request.UserCreateRequestDto;
import capstone.capstone2024.domain.user.dto.response.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<User> create(@RequestBody UserCreateRequestDto userCreateRequestDto){
        return ResponseEntity.ok(userService.create(userCreateRequestDto));
    }

}
