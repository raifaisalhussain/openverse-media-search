package com.mediaapp.controller;

import com.mediaapp.model.User;
import com.mediaapp.repository.UserRepository;
import com.mediaapp.service.MediaService;
import com.mediaapp.util.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestTemplate;
import com.mediaapp.security.JWTFilter;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MediaController.class, excludeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JWTFilter.class))
@AutoConfigureMockMvc
class MediaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MediaService mediaService;
    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private RestTemplate restTemplate;
    @MockitoBean
    private JWTUtil jwtUtil;

    @Test
    void searchMedia_success() throws Exception {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");

        Map<String, Object> response = Map.of("totalResults", 1, "media", Collections.singletonList(Map.of("title", "Sample Image", "imageUrl", "https://example.com/image.jpg")));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(mediaService.searchMedia(eq("nature"), any(), any(), any(), eq(1), eq(mockUser))).thenReturn(response);

        mockMvc.perform(get("/api/media/search").param("query", "nature").param("page", "1").with(user("testuser").roles("USER")).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.totalResults").value(1)).andExpect(jsonPath("$.media[0].title").value("Sample Image")).andExpect(jsonPath("$.media[0].imageUrl").value("https://example.com/image.jpg"));
    }

    @Test
    void searchMedia_noResults() throws Exception {
        User mockUser = new User();
        mockUser.setUsername("testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(mediaService.searchMedia(eq("unknown"), any(), any(), any(), eq(1), eq(mockUser))).thenReturn(Collections.singletonMap("error", "No results found"));

        mockMvc.perform(get("/api/media/search").param("query", "unknown").param("page", "1").with(user("testuser").roles("USER")).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.error").value("No results found"));
    }

    @Test
    void searchMedia_missingQuery() throws Exception {
        mockMvc.perform(get("/api/media/search").param("page", "1").with(user("testuser").roles("USER")).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.error").value("Missing Parameter")).andExpect(jsonPath("$.message").value("Query parameter is missing"));

        verifyNoInteractions(mediaService);
    }

    @TestConfiguration
    static class SecurityTestConfig {
        @Bean
        SecurityFilterChain testFilterChain(HttpSecurity http) throws Exception {
            return http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(reg -> reg.anyRequest().permitAll()).build();
        }
    }

    @RestControllerAdvice
    static class RestErrorHandler {
        @ExceptionHandler(MissingServletRequestParameterException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        public Map<String, String> handleMissingParam(MissingServletRequestParameterException ex) {
            return Map.of("error", ex.getParameterName() + " parameter is missing");
        }
    }

}
