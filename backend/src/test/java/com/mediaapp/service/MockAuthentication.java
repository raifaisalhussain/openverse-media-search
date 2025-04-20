package com.mediaapp.service;

import com.mediaapp.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

class MockAuthentication extends UsernamePasswordAuthenticationToken {
    public MockAuthentication(User principal) {
        super(principal, null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        super.setAuthenticated(true);
    }
}