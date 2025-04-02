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

    // fetch search history by username
    @GetMapping("/user/{username}")
    public ResponseEntity<List<SearchHistory>> getUserHistory(@PathVariable String username) {
        List<SearchHistory> history = historyService.getUserHistoryByUsername(username);
        if (history == null) {
            throw new UserNotFoundException("No user found with username: " + username);
        }
        return ResponseEntity.ok(history);
    }

    // optionally still keep numeric if you want:
    @PostMapping("/save")
    public ResponseEntity<?> saveSearch(@RequestParam Long userId, @RequestParam String query) {
        historyService.saveSearch(userId, query);
        return ResponseEntity.ok("Search saved successfully!");
    }

    @DeleteMapping("/delete/{historyId}")
    public ResponseEntity<?> deleteSearch(@PathVariable Long historyId) {
        historyService.deleteSearch(historyId);
        return ResponseEntity.ok("Search deleted successfully!");
    }
}
