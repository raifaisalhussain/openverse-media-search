package com.mediaapp.service;

import com.mediaapp.model.SearchHistory;
import com.mediaapp.model.User;
import com.mediaapp.repository.SearchHistoryRepository;
import com.mediaapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SearchHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(SearchHistoryService.class);

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    public List<SearchHistory> getUserHistory(Long userId) {
        logger.info("Fetching search history for user ID: {}", userId);
        return searchHistoryRepository.findByUserId(userId);
    }

    public List<SearchHistory> getUserHistoryByUsername(String username) {
        logger.info("Fetching search history for username: {}", username);
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            logger.warn("No user found with username: {}", username);
            return Collections.emptyList();
        }

        return searchHistoryRepository.findByUserId(userOpt.get().getId());
    }

    public void saveSearch(Long userId, String query) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            SearchHistory history = new SearchHistory();
            history.setUser(userOpt.get());
            history.setSearchQuery(query);
            history.setTimestamp(LocalDateTime.now());
            searchHistoryRepository.save(history);
            logger.info("Saved search '{}' for user ID {}", query, userId);
        } else {
            logger.warn("Attempted to save search for non-existing user ID: {}", userId);
        }
    }

    public void deleteSearch(Long historyId) {
        logger.info("Deleting search history with ID: {}", historyId);
        searchHistoryRepository.deleteById(historyId);
    }
}
