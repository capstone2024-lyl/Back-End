package capstone.capstone2024.domain.app.controller;

import capstone.capstone2024.domain.app.application.AppService;
import capstone.capstone2024.domain.app.dto.response.AppResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
