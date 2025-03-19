package com.mediaapp.controller;

import com.mediaapp.model.User;
import com.mediaapp.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @GetMapping("/search")
    public ResponseEntity<?> searchMedia(
            @RequestParam String query,
            @RequestParam(required = false) String mediaType,
            @RequestParam(required = false) String license,
            @RequestParam(required = false) String source,
            @RequestParam(defaultValue = "1") int page,
            @AuthenticationPrincipal User user // ✅ Authenticated User (optional)
    ) {
        try {
            Map<String, Object> response = mediaService.searchMedia(query, mediaType, license, source, page, user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error fetching media: " + e.getMessage()));
        }
    }
}
