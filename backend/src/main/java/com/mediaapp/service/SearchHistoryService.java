package com.mediaapp.service;

import com.mediaapp.model.SearchHistory;
import com.mediaapp.model.User;
import com.mediaapp.repository.SearchHistoryRepository;
import com.mediaapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SearchHistoryService {

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    @Autowired
    private UserRepository userRepository;

    public List<SearchHistory> getUserHistory(Long userId) {
        return searchHistoryRepository.findByUserId(userId);
    }

    public List<SearchHistory> getUserHistoryByUsername(String username) {
        // 1) find user by username
        Optional<User> optUser = userRepository.findByUsername(username);
        if (optUser.isEmpty()) {
            return null; // or throw a custom exception
        }
        // 2) fetch history by user ID
        User user = optUser.get();
        return searchHistoryRepository.findByUserId(user.getId());
    }

    public void saveSearch(Long userId, String query) {
        Optional<User> user = userRepository.findById(userId);
        user.ifPresent(value -> {
            SearchHistory history = new SearchHistory();
            history.setUser(value);
            history.setSearchQuery(query);
            searchHistoryRepository.save(history);
        });
    }

    public void deleteSearch(Long historyId) {
        searchHistoryRepository.deleteById(historyId);
    }
}
