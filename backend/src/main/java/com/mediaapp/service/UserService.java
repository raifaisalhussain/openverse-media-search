package com.mediaapp.service;

import com.mediaapp.model.User;
import com.mediaapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<?> register(User user) {
        logger.info("Registering user: {}", user.getUsername());

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            logger.warn("Attempt to register with existing username: {}", user.getUsername());
            return ResponseEntity.badRequest().body(Map.of("error", "Username already exists"));
        }

        if (user.getPassword() == null || user.getPassword().isBlank()) {
            logger.warn("Password is missing during registration for user: {}", user.getUsername());
            return ResponseEntity.badRequest().body(Map.of("error", "Password cannot be empty"));
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(User.Role.USER);
        userRepository.save(user);

        logger.info("User registered successfully: {}", user.getUsername());
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    public ResponseEntity<?> login(User user) {
        logger.info("Attempting login for user: {}", user.getUsername());

        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser.isPresent() && existingUser.get().getPassword() != null && passwordEncoder.matches(user.getPassword(), existingUser.get().getPassword())) {

            logger.info("Login successful for user: {}", user.getUsername());
            return ResponseEntity.ok(Map.of("message", "Login successful"));
        }

        logger.warn("Login failed for user: {}", user.getUsername());
        return ResponseEntity.status(401).body(Map.of("error", "Invalid credentials"));
    }
}
