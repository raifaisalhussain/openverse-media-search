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

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MediaService {

    private static final Logger logger = LoggerFactory.getLogger(MediaService.class);
    private static final String OPENVERSE_API_BASE_URL = "https://api.openverse.org/v1";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    @Transactional
    public Map<String, Object> searchMedia(String query, String mediaType, String license, String source, int page, User user) {
        String endpoint = (mediaType != null && mediaType.equalsIgnoreCase("audio")) ? OPENVERSE_API_BASE_URL + "/audio/" : OPENVERSE_API_BASE_URL + "/images/";

        StringBuilder apiUrl = new StringBuilder(endpoint).append("?q=").append(query).append("&page=").append(page);

        if (license != null && !license.isBlank()) apiUrl.append("&license=").append(license);
        if (source != null && !source.isBlank()) apiUrl.append("&source=").append(source);

        logger.info("Calling Openverse API: {}", apiUrl);

        try {
            Map<String, Object> apiResponse = restTemplate.getForObject(apiUrl.toString(), Map.class);

            if (apiResponse == null || !apiResponse.containsKey("results")) {
                logger.warn("No results or invalid response from Openverse API");
                return Map.of("error", "No results found");
            }

            @SuppressWarnings("unchecked") List<Map<String, Object>> results = (List<Map<String, Object>>) apiResponse.get("results");

            List<Map<String, Object>> formatted = results.stream().map(result -> {
                Map<String, Object> f = new HashMap<>();
                f.put("title", result.get("title"));
                f.put("mediaType", mediaType != null && mediaType.equalsIgnoreCase("audio") ? "audio" : "image");
                f.put("thumbnail", result.get("thumbnail"));
                f.put("url", result.get("url"));
                f.put("license", result.get("license"));
                f.put("creator", result.get("creator"));
                f.put("sourceUrl", result.get("foreign_landing_url"));
                return f;
            }).collect(Collectors.toList());

            if (user != null && page == 1) {
                logger.info("Saving search history for user: {}", user.getUsername());
                searchHistoryRepository.save(new SearchHistory(user, query));
            }

            int totalResults = Optional.ofNullable((Number) apiResponse.get("result_count")).map(Number::intValue).orElse(0);
            int pageSize = Optional.ofNullable((Number) apiResponse.get("page_size")).map(Number::intValue).orElse(20);
            int totalPages = (totalResults + pageSize - 1) / pageSize;

            String nextPage = (page < totalPages) ? "/api/media/search?query=" + query + "&page=" + (page + 1) : null;

            String previousPage = (page > 1) ? "/api/media/search?query=" + query + "&page=" + (page - 1) : null;

            Map<String, Object> response = new HashMap<>();
            response.put("totalResults", totalResults);
            response.put("pageSize", pageSize);
            response.put("page", page);
            response.put("nextPage", nextPage);
            response.put("previousPage", previousPage);
            response.put("media", formatted);

            return response;

        } catch (Exception e) {
            logger.error("Failed to fetch media from Openverse API", e);
            return Map.of("error", "Failed to fetch media", "exception", e.getClass().getSimpleName(), "message", Optional.ofNullable(e.getMessage()).orElse("Unexpected error"));
        }
    }
}
