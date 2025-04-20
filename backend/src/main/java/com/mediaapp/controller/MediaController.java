package com.mediaapp.controller;

import com.mediaapp.model.User;
import com.mediaapp.repository.UserRepository;
import com.mediaapp.service.MediaService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> searchMedia(@RequestParam String query, @RequestParam(required = false) String mediaType, @RequestParam(required = false) String license, @RequestParam(required = false) String source, @RequestParam(defaultValue = "1") int page, @AuthenticationPrincipal UserDetails userDetails, HttpServletRequest request) {
        try {
            User user = null;

            if (userDetails != null) {
                user = userRepository.findByUsername(userDetails.getUsername()).orElse(null);
            }

            if (user == null) {
                Object sessionUser = request.getSession().getAttribute("user");
                if (sessionUser instanceof User) {
                    user = (User) sessionUser;
                }
            }

            logger.debug("Media search request received - query: {}, mediaType: {}, license: {}, source: {}, page: {}, user: {}", query, mediaType, license, source, page, user != null ? user.getUsername() : "anonymous");

            Map<String, Object> response = mediaService.searchMedia(query, mediaType, license, source, page, user);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error occurred during media search", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Media search failed", "message", e.getMessage()));
        }
    }
}
