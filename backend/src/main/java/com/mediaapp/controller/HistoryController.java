package com.mediaapp.controller;

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

    // ✅ Get search history for a specific user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SearchHistory>> getUserHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(historyService.getUserHistory(userId));
    }

    // ✅ Save a new search query
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
