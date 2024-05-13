package capstone.capstone2024.domain.app.controller;

import capstone.capstone2024.domain.app.application.AppService;
import capstone.capstone2024.domain.app.dto.request.AppUsageRequestDto;
import capstone.capstone2024.domain.app.dto.response.AppResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/app")
public class AppController {
    private final AppService appService;
    @GetMapping("/{loginId}")
    public ResponseEntity<List<AppResponseDto>> findTop3App(@PathVariable String loginId){
        return ResponseEntity.ok(appService.findTop3App(loginId));

    }

    @PostMapping("/{loginId}/appUsage")
    public ResponseEntity<String> createAppUsage(
            @PathVariable String loginId,
            @Valid @RequestBody AppUsageRequestDto appUsageRequestDto
    ){
        return ResponseEntity.ok(appService.createAppUsage(loginId, appUsageRequestDto));
    }
}
