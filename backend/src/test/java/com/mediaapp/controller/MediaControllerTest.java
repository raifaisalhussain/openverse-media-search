package com.mediaapp.controller;

import com.mediaapp.model.User;
import com.mediaapp.service.MediaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MediaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MediaService mediaService;

    @InjectMocks
    private MediaController mediaController;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(mediaController).build();

        // Create a mock user
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        mockUser.setPassword("password123");
    }

    @Test
    void testSearchMedia_Success() throws Exception {
        // Mock response from service
        Map<String, Object> mockResponse = Map.of(
                "totalResults", 1,
                "media", Collections.singletonList(
                        Map.of(
                                "title", "Sample Image",
                                "imageUrl", "https://example.com/image.jpg"
                        )
                )
        );

        when(mediaService.searchMedia(anyString(), any(), any(), any(), anyInt(), any())).thenReturn(mockResponse);

        mockMvc.perform(get("/api/media/search")
                        .param("query", "nature")
                        .param("page", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalResults").value(1))
                .andExpect(jsonPath("$.media[0].title").value("Sample Image"))
                .andExpect(jsonPath("$.media[0].imageUrl").value("https://example.com/image.jpg"));

        verify(mediaService, times(1)).searchMedia(eq("nature"), any(), any(), any(), eq(1), any());
    }

    @Test
    void testSearchMedia_NoResults() throws Exception {
        when(mediaService.searchMedia(anyString(), any(), any(), any(), anyInt(), any()))
                .thenReturn(Collections.singletonMap("error", "No results found"));

        mockMvc.perform(get("/api/media/search")
                        .param("query", "unknown")
                        .param("page", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("No results found"));

        verify(mediaService, times(1)).searchMedia(eq("unknown"), any(), any(), any(), eq(1), any());
    }
}
