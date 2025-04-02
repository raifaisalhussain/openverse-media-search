package com.mediaapp.controller;

import com.mediaapp.model.User;
import com.mediaapp.repository.UserRepository;
import com.mediaapp.service.MediaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private static final Logger logger = LoggerFactory.getLogger(MediaController.class);

    @Autowired
    private MediaService mediaService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/search")
    public ResponseEntity<?> searchMedia(
            @RequestParam String query,
            @RequestParam(required = false) String mediaType,
            @RequestParam(required = false) String license,
            @RequestParam(required = false) String source,
            @RequestParam(defaultValue = "1") int page,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = null;

        if (userDetails != null) {
            logger.info("Authenticated request by: {}", userDetails.getUsername());
            user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);

            if (user == null) {
                logger.warn("User not found in DB for username: {}", userDetails.getUsername());
            }
        } else {
            logger.warn("No authenticated user found in request.");
        }

        Map<String, Object> response = mediaService.searchMedia(query, mediaType, license, source, page, user);
        return ResponseEntity.ok(response);
    }

}
