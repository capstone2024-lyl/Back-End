package capstone.capstone2024.domain.photo.controller;


import capstone.capstone2024.domain.photo.application.PhotoService;
import capstone.capstone2024.domain.photo.dto.request.PhotoCreateRequestDto;
import capstone.capstone2024.domain.photo.dto.response.PhotoResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/photo")
public class PhotoController {
    private final PhotoService photoService;

    @PostMapping("/saveResult")
    public ResponseEntity<String> saveResult(@Valid @RequestBody PhotoCreateRequestDto photoCreateRequestDto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        photoService.saveResult(photoCreateRequestDto, loginId);
        return ResponseEntity.status(HttpStatus.CREATED).body("created");
    }


    @GetMapping("/getResult")
    public ResponseEntity<PhotoResponseDto> getResult() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();
        PhotoResponseDto responseDto = photoService.getResult(loginId);
        return ResponseEntity.ok(responseDto);
    }


}
