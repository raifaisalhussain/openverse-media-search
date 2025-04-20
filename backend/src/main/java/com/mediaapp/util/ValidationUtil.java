package com.mediaapp.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class ValidationUtil {

    private static final Logger logger = LoggerFactory.getLogger(ValidationUtil.class);

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{8,}$";

    public static boolean isValidEmail(String email) {
        try {
            if (email == null || email.isBlank()) {
                logger.warn("Email validation failed: input is null or blank");
                return false;
            }
            return Pattern.compile(EMAIL_REGEX).matcher(email).matches();
        } catch (Exception e) {
            logger.error("Email validation error: {}", e.getMessage());
            return false;
        }
    }

    public static boolean isValidPassword(String password) {
        try {
            if (password == null || password.isBlank()) {
                logger.warn("Password validation failed: input is null or blank");
                return false;
            }
            return Pattern.compile(PASSWORD_REGEX).matcher(password).matches();
        } catch (Exception e) {
            logger.error("Password validation error: {}", e.getMessage());
            return false;
        }
    }
}
