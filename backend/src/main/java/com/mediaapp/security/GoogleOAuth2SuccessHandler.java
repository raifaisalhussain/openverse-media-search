package com.mediaapp.security;

import com.mediaapp.model.User;
import com.mediaapp.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(GoogleOAuth2SuccessHandler.class);
    private final UserRepository userRepository;

    public GoogleOAuth2SuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

        if (email == null || email.isBlank()) {
            logger.error("OAuth2 authentication succeeded but email is missing from user info");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Email not found in OAuth2 response.");
            return;
        }

        User user = userRepository.findByUsername(email).orElseGet(() -> {
            logger.info("New OAuth2 user detected: {}. Creating user record.", email);
            User newUser = new User();
            newUser.setUsername(email);
            newUser.setPassword("");
            newUser.setRole(User.Role.USER);
            return userRepository.save(newUser);
        });

        logger.info("User '{}' authenticated successfully via Google OAuth2", email);

        request.getSession().setAttribute("user", user);
        response.sendRedirect("http://localhost:3000/");
    }
}
