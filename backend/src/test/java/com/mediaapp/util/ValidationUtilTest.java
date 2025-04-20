package com.mediaapp.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationUtilTest {

    @Test
    void testValidEmail() {
        assertTrue(ValidationUtil.isValidEmail("test@example.com"));
        assertFalse(ValidationUtil.isValidEmail("invalid-email"));
    }

    @Test
    void testValidPassword() {
        assertTrue(ValidationUtil.isValidPassword("Password1"));
        assertFalse(ValidationUtil.isValidPassword("short"));
    }
}