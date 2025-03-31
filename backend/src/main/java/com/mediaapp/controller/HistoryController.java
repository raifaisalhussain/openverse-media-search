package com.mediaapp.controller;

import com.mediaapp.exception.UserNotFoundException;
import com.mediaapp.model.SearchHistory;
import com.mediaapp.service.SearchHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/history")
public class HistoryController {

    @Autowired
    private SearchHistoryService historyService;

    // ✅ Get search history for a specific user BY USERNAME
    @GetMapping("/user/{username}")
    public ResponseEntity<List<SearchHistory>> getUserHistory(@PathVariable String username) {
        // calls a new service method that finds user by username, then fetches search history by userId
        List<SearchHistory> history = historyService.getUserHistoryByUsername(username);
        if (history == null) {
            throw new UserNotFoundException("User not found: " + username);
        }
        return ResponseEntity.ok(history);
    }

    // ✅ Save a new search query (still uses numeric userId)
    @PostMapping("/save")
    public ResponseEntity<?> saveSearch(@RequestParam Long userId, @RequestParam String query) {
        historyService.saveSearch(userId, query);
        return ResponseEntity.ok("Search saved successfully!");
    }

    // ✅ Delete a search entry by ID
    @DeleteMapping("/delete/{historyId}")
    public ResponseEntity<?> deleteSearch(@PathVariable Long historyId) {
        historyService.deleteSearch(historyId);
        return ResponseEntity.ok("Search deleted successfully!");
    }
}
