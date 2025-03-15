package com.mediaapp.controller;

import com.mediaapp.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    @Autowired
    private MediaService mediaService;

    @GetMapping("/search")
    public ResponseEntity<?> searchMedia(
            @RequestParam String query,
            @RequestParam(required = false) String mediaType, // image, audio
            @RequestParam(required = false) String license, // CC0, BY, BY-SA
            @RequestParam(required = false) String source // Flickr, Wikimedia
    ) {
        try {
            String response = mediaService.searchMedia(query, mediaType, license, source);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching media: " + e.getMessage());
        }
    }
}
