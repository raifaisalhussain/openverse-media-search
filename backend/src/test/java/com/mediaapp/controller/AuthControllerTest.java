package com.mediaapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediaapp.model.User;
import com.mediaapp.util.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = "spring.main.allow-bean-definition-overriding=true")
@AutoConfigureMockMvc
@Transactional
@Rollback
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void registerEndpointShouldReturn200() throws Exception {
        User payload = new User();
        payload.setUsername("newuser_" + UUID.randomUUID());
        payload.setPassword("newPass123");
        payload.setRole(User.Role.USER);

        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(payload))).andExpect(status().isOk()).andExpect(content().string(containsString("registered")));
    }

    @Test
    void loginShouldReturnJwt() throws Exception {

        String username = "test_" + UUID.randomUUID();
        String rawPwd = "TestPass123";

        User reg = new User();
        reg.setUsername(username);
        reg.setPassword(rawPwd);
        reg.setRole(User.Role.USER);

        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(reg))).andExpect(status().isOk());

        User login = new User();
        login.setUsername(username);
        login.setPassword(rawPwd);

        mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(login))).andExpect(status().isOk()).andExpect(content().string(containsString(".")));
    }

    @TestConfiguration
    static class JwtUtilOverride {
        @Bean
        @Primary
        JWTUtil jwtUtil() {
            return new JWTUtil();
        }
    }
}
