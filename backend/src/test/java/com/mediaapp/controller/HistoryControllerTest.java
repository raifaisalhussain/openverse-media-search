package com.mediaapp.controller;

import com.mediaapp.model.SearchHistory;
import com.mediaapp.service.SearchHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class HistoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SearchHistoryService historyService;

    @InjectMocks
    private HistoryController historyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(historyController).build();
    }

    @Test
    void testGetUserHistory() throws Exception {
        when(historyService.getUserHistoryByUsername("testuser")).thenReturn(List.of(new SearchHistory()));

        mockMvc.perform(get("/api/history/user/testuser")).andExpect(status().isOk());
    }

    @Test
    void testSaveSearch() throws Exception {
        Long userId = 1L;
        String query = "cats";
        doNothing().when(historyService).saveSearch(userId, query);

        mockMvc.perform(post("/api/history/save").param("userId", userId.toString()).param("query", query)).andExpect(status().isOk()).andExpect(content().string("Search saved successfully!"));

        verify(historyService).saveSearch(userId, query);
    }

    @Test
    void testDeleteSearch() throws Exception {
        Long searchId = 1L;
        doNothing().when(historyService).deleteSearch(searchId);

        mockMvc.perform(delete("/api/history/delete/" + searchId)).andExpect(status().isOk()).andExpect(content().string("Search deleted successfully!"));

        verify(historyService).deleteSearch(searchId);
    }

    @Test
    void testGetUserHistoryNotFound() throws Exception {
        when(historyService.getUserHistoryByUsername("nonexistentuser")).thenReturn(List.of());

        mockMvc.perform(get("/api/history/user/nonexistentuser")).andExpect(status().isOk()).andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$").isEmpty());
    }
}