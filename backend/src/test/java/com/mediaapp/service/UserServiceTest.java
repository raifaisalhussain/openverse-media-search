package com.mediaapp.service;

import com.mediaapp.model.User;
import com.mediaapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegister_NewUser_Success() {
        User user = new User();
        user.setUsername("newuser");
        user.setPassword("password");

        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenReturn(user);

        ResponseEntity<?> response = userService.register(user);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void testRegister_ExistingUser_Fail() {
        User user = new User();
        user.setUsername("existinguser");

        when(userRepository.findByUsername("existinguser")).thenReturn(Optional.of(user));

        ResponseEntity<?> response = userService.register(user);
        assertEquals(400, response.getStatusCodeValue());
    }
}