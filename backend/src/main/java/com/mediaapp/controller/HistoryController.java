package com.mediaapp.controller;

import com.mediaapp.exception.UserNotFoundException;
import com.mediaapp.model.SearchHistory;
import com.mediaapp.service.SearchHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    private static final Logger logger = LoggerFactory.getLogger(HistoryController.class);

    @Autowired
    private SearchHistoryService historyService;

    @GetMapping("/user/{username}")
    public ResponseEntity<?> getUserHistory(@PathVariable String username) {
        try {
            logger.debug("Getting search history for user: {}", username);
            List<SearchHistory> history = historyService.getUserHistoryByUsername(username);
            if (history == null || history.isEmpty()) {
                logger.debug("No history found for user: {}", username);
                return ResponseEntity.ok(Collections.emptyList());
            }
            return ResponseEntity.ok(history);
        } catch (UserNotFoundException e) {
            logger.warn("User not found: {}", username, e);
            return ResponseEntity.ok(Collections.emptyList());
        } catch (Exception e) {
            logger.error("Error retrieving search history for user: {}", username, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to retrieve search history", "message", e.getMessage()));
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> saveSearch(@RequestParam Long userId, @RequestParam String query) {
        try {
            historyService.saveSearch(userId, query);
            return ResponseEntity.ok("Search saved successfully!");
        } catch (Exception e) {
            logger.error("Error saving search history", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to save search", "message", e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{historyId}")
    public ResponseEntity<?> deleteSearch(@PathVariable Long historyId) {
        try {
            historyService.deleteSearch(historyId);
            return ResponseEntity.ok("Search deleted successfully!");
        } catch (Exception e) {
            logger.error("Error deleting search history", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to delete search", "message", e.getMessage()));
        }
    }
}
