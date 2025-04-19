package com.mediaapp.controller;

import com.mediaapp.exception.GlobalExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MeController {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @GetMapping("/api/me")
    public Map<String, Object> me(@AuthenticationPrincipal OAuth2User principal) {
        try {
            if (principal == null) {
                return Map.of("authenticated", false);
            }

            String email = principal.getAttribute("email");
            String name = principal.getAttribute("name");
            String picture = principal.getAttribute("picture");

            return Map.of("authenticated", true, "email", email, "name", name, "picture", picture, "attributes", principal.getAttributes());
        } catch (Exception e) {
            log.error("Error retrieving user data", e);
            return Map.of("authenticated", false, "error", "Failed to retrieve user data");
        }
    }
}
