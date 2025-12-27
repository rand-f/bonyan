package com.example.bnyan.Stability;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/image")
public class ImageTestController {

    private final ImageGenerationService imageGenerationService;

    @PostMapping("/generate/draft")
    public ResponseEntity<?> generateDraft(@RequestBody String description) {

        byte[] image = imageGenerationService.generateDraftBuilding(description);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"draft-building.jpeg\"")
                .contentType(MediaType.IMAGE_JPEG)
                .body(image);
    }
}
