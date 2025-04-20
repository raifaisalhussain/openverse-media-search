package com.mediaapp.service;

import com.mediaapp.exception.UserNotFoundException;
import com.mediaapp.model.SearchHistory;
import com.mediaapp.model.User;
import com.mediaapp.repository.SearchHistoryRepository;
import com.mediaapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
        User user = userRepository.findByUsername(username).orElseThrow(() -> {
            logger.warn("User not found with username: {}", username);
            return new UserNotFoundException("User not found with username: " + username);
        });

        return searchHistoryRepository.findByUserId(user.getId());
    }

    public void saveSearch(Long userId, String query) {
        logger.info("Saving search for user ID: {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() -> {
            logger.warn("Attempted to save search for non-existing user ID: {}", userId);
            return new UserNotFoundException("User not found with ID: " + userId);
        });

        SearchHistory history = new SearchHistory();
        history.setUser(user);
        history.setSearchQuery(query);
        history.setTimestamp(LocalDateTime.now());
        searchHistoryRepository.save(history);

        logger.info("Saved search '{}' for user ID {}", query, userId);
    }

    public void deleteSearch(Long historyId) {
        logger.info("Deleting search history with ID: {}", historyId);
        searchHistoryRepository.deleteById(historyId);
    }
}
