package com.mediaapp.service;

import com.mediaapp.model.User;
import com.mediaapp.repository.SearchHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // ✅ Handles mock initialization
class MediaServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private SearchHistoryRepository searchHistoryRepository;

    @InjectMocks
    private MediaService mediaService;

    @Test
    void testSearchMedia_AuthenticatedUser_SavesHistory() {
        // 1. Mock authenticated user
        User user = new User();
        user.setId(1L);
        SecurityContextHolder.getContext().setAuthentication(
                new MockAuthentication(user)
        );

        // 2. Mock API response
        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenReturn(Map.of(
                        "results", List.of(Map.of("title", "Test Image")),
                        "result_count", 1,
                        "page_size", 20
                ));

        // 3. Execute
        Map<String, Object> result = mediaService.searchMedia(
                "nature", null, null, null, 1, user
        );

        // 4. Verify
        assertTrue(result.containsKey("media"));
        verify(searchHistoryRepository, times(1)).save(any());
    }

    @Test
    void testSearchMedia_UnauthenticatedUser_NoHistorySaved() {
        // 1. Mock API response
        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenReturn(Map.of("results", List.of()));

        // 2. Execute
        Map<String, Object> result = mediaService.searchMedia(
                "nature", null, null, null, 1, null
        );

        // 3. Verify
        assertTrue(result.containsKey("media"));
        verify(searchHistoryRepository, never()).save(any());
    }
}