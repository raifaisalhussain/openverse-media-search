package com.mediaapp.service;

import com.mediaapp.model.User;
import com.mediaapp.repository.SearchHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MediaServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private SearchHistoryRepository searchHistoryRepository;

    @InjectMocks
    private MediaService mediaService;

    @Test
    void testSearchMedia_AuthenticatedUser_SavesHistory() {
        User user = new User();
        user.setId(1L);

        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(Map.of("results", List.of(Map.of("title", "Test Image")), "result_count", 1, "page_size", 20));

        Map<String, Object> result = mediaService.searchMedia("nature", null, null, null, 1, user);

        assertTrue(result.containsKey("media"));
        verify(searchHistoryRepository, times(1)).save(any());
    }

    @Test
    void testSearchMedia_UnauthenticatedUser_NoHistorySaved() {
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenReturn(Map.of("results", List.of()));

        Map<String, Object> result = mediaService.searchMedia("nature", null, null, null, 1, null);

        assertTrue(result.containsKey("media"));
        verify(searchHistoryRepository, never()).save(any());
    }

    @Test
    void testSearchMedia_ApiFailure_ReturnsError() {
        when(restTemplate.getForObject(anyString(), eq(Map.class))).thenThrow(new RuntimeException("API down"));

        Map<String, Object> result = mediaService.searchMedia("nature", null, null, null, 1, null);

        assertTrue(result.containsKey("error"));
        assertTrue(result.containsKey("exception"));

        String exception = String.valueOf(result.get("exception")).toLowerCase();
        assertTrue(exception.contains("runtime"), () -> "expected exception name to contain 'runtime' but was: " + exception);
    }

}
