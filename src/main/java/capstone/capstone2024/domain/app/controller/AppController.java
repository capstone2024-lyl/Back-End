package capstone.capstone2024.domain.app.controller;

import capstone.capstone2024.domain.app.application.AppService;
import capstone.capstone2024.domain.app.dto.request.AppUsageCreateRequestDto;
import capstone.capstone2024.domain.app.dto.response.AppResponseDto;
import capstone.capstone2024.domain.app.dto.response.AppsResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/app")
public class AppController {
    private final AppService appService;
    @GetMapping("/findTop10")
    public ResponseEntity<AppsResponseDto> findTop10App(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        return ResponseEntity.ok(appService.findTop10App(loginId));
    }

    @PostMapping("/appUsage")
    public ResponseEntity<String> createAppUsage(
            @Valid @RequestBody List<AppUsageCreateRequestDto> appUsageCreateRequestDto
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        return ResponseEntity.ok(appService.createAppUsage(loginId, appUsageCreateRequestDto));
    }
}
