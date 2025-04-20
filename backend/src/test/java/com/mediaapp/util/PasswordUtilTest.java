package com.mediaapp.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilTest {

    @Test
    void testHashAndVerifyPassword() {
        String raw = "Password123";
        String hashed = PasswordUtil.hashPassword(raw);

        assertNotNull(hashed);
        assertTrue(PasswordUtil.verifyPassword(raw, hashed));
    }
}