package capstone.capstone2024.domain.storage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import capstone.capstone2024.domain.storage.application.StorageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/storage")
public class StorageController {
    private final StorageService storageService;

    @PostMapping(value = "/s3/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> s3Upload(
            @RequestParam("file") MultipartFile image
    ){
        String profileImage = storageService.upload(image);
        return ResponseEntity.ok(profileImage);
    }

    @GetMapping("/s3/delete")
    public ResponseEntity<?> s3delete(@RequestParam String addr){
        storageService.deleteImageFromS3(addr);
        return ResponseEntity.ok(null);
    }

}
