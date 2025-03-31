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

    private final String OPENVERSE_API_BASE_URL = "https://api.openverse.org/v1/images/";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SearchHistoryRepository searchHistoryRepository;

    @Transactional  // Add this
    public Map<String, Object> searchMedia(String query, String mediaType, String license, String source, int page, User user) {
        StringBuilder apiUrl = new StringBuilder(OPENVERSE_API_BASE_URL + "?q=" + query + "&page=" + page);

        if (mediaType != null) apiUrl.append("&media_type=").append(mediaType);
        if (license != null) apiUrl.append("&license=").append(license);
        if (source != null) apiUrl.append("&source=").append(source);

        try {
            logger.info("Calling Openverse: {}", apiUrl);
            Map<String, Object> apiResponse = restTemplate.getForObject(apiUrl.toString(), Map.class);

            if (apiResponse == null) {
                return Map.of("error", "No response from Openverse");
            }

            Object rawResults = apiResponse.get("results");
            if (!(rawResults instanceof List)) {
                return Map.of("error", "Unexpected API format: 'results' missing or invalid");
            }

            List<Map<String, Object>> results = (List<Map<String, Object>>) rawResults;

            List<Map<String, Object>> formattedResults = results.stream().map(result -> {
                Map<String, Object> formatted = new HashMap<>();
                formatted.put("title", result.get("title"));
                formatted.put("imageUrl", result.get("url"));
                formatted.put("creator", result.get("creator"));
                formatted.put("creatorUrl", result.get("creator_url"));
                formatted.put("license", result.get("license"));
                formatted.put("licenseUrl", result.get("license_url"));
                formatted.put("source", result.get("source"));
                formatted.put("thumbnail", result.get("thumbnail"));
                formatted.put("detailUrl", result.get("detail_url"));
                formatted.put("height", result.get("height"));
                formatted.put("width", result.get("width"));
                return formatted;
            }).collect(Collectors.toList());

            // Save Search Query if User is Authenticated
            if (user != null) {
                logger.info("Saving search history for user: {}", user.getUsername());
                SearchHistory history = new SearchHistory();
                history.setUser(user);
                history.setSearchQuery(query);
                searchHistoryRepository.save(history);
            } else {
                logger.warn("User is null. Search history not saved.");
            }

            // Pagination Logic with safe null handling
            Number totalResultsNum = (Number) apiResponse.get("result_count");
            Number pageSizeNum = (Number) apiResponse.get("page_size");

            int totalResults = totalResultsNum != null ? totalResultsNum.intValue() : 0;
            int pageSize = pageSizeNum != null ? pageSizeNum.intValue() : 20;
            int totalPages = (totalResults + pageSize - 1) / pageSize;


            String nextPageUrl = (page < totalPages) ?
                    "http://localhost:8080/api/media/search?query=" + query + "&page=" + (page + 1) : null;

            String previousPageUrl = (page > 1) ?
                    "http://localhost:8080/api/media/search?query=" + query + "&page=" + (page - 1) : null;


            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("totalResults", totalResults);
            resultMap.put("pageSize", pageSize);
            resultMap.put("page", page);
            resultMap.put("nextPage", nextPageUrl);      // can be null, no crash
            resultMap.put("previousPage", previousPageUrl); // can be null, no crash
            resultMap.put("media", formattedResults);

            return resultMap;

        } catch (Exception e) {
            logger.error("Failed to fetch media from Openverse API", e);

            String details = e.getMessage() != null ? e.getMessage() : "No details available";

            return Map.of(
                    "error", "Exception: " + e.getClass().getSimpleName(),
                    "details", details
            );
        }

    }
}
