package com.mediaapp.service;

import com.mediaapp.model.User;

class MockAuthentication extends org.springframework.security.authentication.UsernamePasswordAuthenticationToken {
    public MockAuthentication(User principal) {
        super(principal, null);
    }
}