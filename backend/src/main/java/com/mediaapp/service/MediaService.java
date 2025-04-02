package com.mediaapp.service;

import com.mediaapp.model.SearchHistory;
import com.mediaapp.model.User;
import com.mediaapp.repository.SearchHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MediaService {

    private static final Logger logger = LoggerFactory.getLogger(MediaService.class);

    private final String OPENVERSE_API_BASE_URL = "https://api.openverse.org/v1";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    @Transactional
    public Map<String, Object> searchMedia(String query, String mediaType, String license, String source, int page, User user) {
        // default to images if not specified
        String endpoint = (mediaType != null && mediaType.equalsIgnoreCase("audio"))
                ? OPENVERSE_API_BASE_URL + "/audio/"
                : OPENVERSE_API_BASE_URL + "/images/";

        StringBuilder apiUrl = new StringBuilder(endpoint + "?q=" + query + "&page=" + page);
        if (license != null && !license.isBlank()) apiUrl.append("&license=").append(license);
        if (source != null && !source.isBlank()) apiUrl.append("&source=").append(source);

        logger.info("Calling Openverse: {}", apiUrl);

        try {
            Map<String, Object> apiResponse = restTemplate.getForObject(apiUrl.toString(), Map.class);
            if (apiResponse == null || !apiResponse.containsKey("results")) {
                return Map.of("error", "No results found");
            }

            List<Map<String, Object>> results = (List<Map<String, Object>>) apiResponse.get("results");

            // format them for your React app
            List<Map<String, Object>> formatted = results.stream().map(r -> {
                Map<String, Object> f = new HashMap<>();
                f.put("title", r.get("title"));
                f.put("mediaType", (mediaType != null && mediaType.equalsIgnoreCase("audio")) ? "audio" : "image");
                f.put("thumbnail", r.get("thumbnail"));
                f.put("url", r.get("url"));  // for audio or image
                f.put("license", r.get("license"));
                f.put("creator", r.get("creator"));
                f.put("sourceUrl", r.get("foreign_landing_url"));
                return f;
            }).collect(Collectors.toList());

            // saving user’s search automatically
            if (user != null && page == 1) {
                logger.info("Saving search history for user: {}", user.getUsername());
                SearchHistory history = new SearchHistory();
                history.setUser(user);
                history.setSearchQuery(query);
                history.setTimestamp(LocalDateTime.now());
                searchHistoryRepository.save(history);
            }

            // pagination
            Number totalResultsNum = (Number) apiResponse.get("result_count");
            Number pageSizeNum = (Number) apiResponse.get("page_size");

            int totalResults = (totalResultsNum != null) ? totalResultsNum.intValue() : 0;
            int pageSize = (pageSizeNum != null) ? pageSizeNum.intValue() : 20;
            int totalPages = (totalResults + pageSize - 1) / pageSize;

            String nextPage = (page < totalPages)
                    ? "/api/media/search?query=" + query + "&page=" + (page + 1)
                    : null;
            String prevPage = (page > 1)
                    ? "/api/media/search?query=" + query + "&page=" + (page - 1)
                    : null;

            Map<String, Object> response = new HashMap<>();
            response.put("totalResults", totalResults);
            response.put("pageSize", pageSize);
            response.put("page", page);
            response.put("nextPage", nextPage);
            response.put("previousPage", prevPage);
            response.put("media", formatted);

            return response;

        } catch (Exception e) {
            logger.error("Failed to fetch media from Openverse", e);
            String details = (e.getMessage() != null) ? e.getMessage() : "No details";
            return Map.of("error", "Exception: " + e.getClass().getSimpleName(), "details", details);
        }
    }
}
