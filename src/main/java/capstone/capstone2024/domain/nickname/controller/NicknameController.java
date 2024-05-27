package capstone.capstone2024.domain.nickname.controller;

import capstone.capstone2024.domain.nickname.application.NicknameService;
import capstone.capstone2024.domain.nickname.domain.Nickname;
import capstone.capstone2024.domain.user.dto.response.UserNicknameResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/nickname")
public class NicknameController {
    private final NicknameService nicknameService;
    @PostMapping("")
    public ResponseEntity<UserNicknameResponseDto> addNickname(@RequestParam Nickname nickname) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        return ResponseEntity.ok(nicknameService.addNickname(loginId, nickname));
    }

}
