package com.mediaapp.service;

import com.mediaapp.model.User;
import com.mediaapp.repository.SearchHistoryRepository;
import org.junit.jupiter.api.Assertions;
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
        SecurityContextHolder.getContext().setAuthentication(
                new MockAuthentication(user)
        );

        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenReturn(Map.of(
                        "results", List.of(Map.of("title", "Test Image")),
                        "result_count", 1,
                        "page_size", 20
                ));

        Map<String, Object> result = mediaService.searchMedia(
                "nature", null, null, null, 1, user
        );

        assertTrue(result.containsKey("media"));
        verify(searchHistoryRepository, times(1)).save(any());
    }

    @Test
    void testSearchMedia_UnauthenticatedUser_NoHistorySaved() {
        when(restTemplate.getForObject(anyString(), eq(Map.class)))
                .thenReturn(Map.of("results", List.of()));

        Map<String, Object> result = mediaService.searchMedia(
                "nature", null, null, null, 1, null
        );

        assertTrue(result.containsKey("media"));
        verify(searchHistoryRepository, never()).save(any());
    }

    @Test
    void testSearchMediaNullUser() {
        MediaService mediaService = Mockito.mock(MediaService.class);

        Mockito.when(mediaService.searchMedia("test", "image", null, null, 1, null))
                .thenReturn(Map.of("media", "someValue"));

        var result = mediaService.searchMedia("test", "image", null, null, 1, null);
        Assertions.assertNotNull(result);
        Assertions.assertTrue(result.containsKey("media"));
    }
}