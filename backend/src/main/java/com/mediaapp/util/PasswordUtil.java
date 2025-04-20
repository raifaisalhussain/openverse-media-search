package com.mediaapp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {

    private static final Logger logger = LoggerFactory.getLogger(PasswordUtil.class);
    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String hashPassword(String password) {
        try {
            if (password == null || password.isBlank()) {
                throw new IllegalArgumentException("Password must not be null or blank");
            }
            return passwordEncoder.encode(password);
        } catch (Exception e) {
            logger.error("Failed to hash password: {}", e.getMessage());
            throw e;
        }
    }

    public static boolean verifyPassword(String rawPassword, String hashedPassword) {
        try {
            if (rawPassword == null || hashedPassword == null) {
                logger.warn("One or both passwords are null during verification");
                return false;
            }
            return passwordEncoder.matches(rawPassword, hashedPassword);
        } catch (Exception e) {
            logger.error("Password verification failed: {}", e.getMessage());
            return false;
        }
    }
}
