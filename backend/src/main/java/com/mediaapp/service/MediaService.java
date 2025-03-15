package com.mediaapp.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MediaService {

    private final String OPENVERSE_API_BASE_URL = "https://api.openverse.org/v1/images/";

    private final RestTemplate restTemplate;

    public MediaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String searchMedia(String query, String mediaType, String license, String source) {
        StringBuilder apiUrl = new StringBuilder(OPENVERSE_API_BASE_URL + "?q=" + query);

        if (mediaType != null) {
            apiUrl.append("&media_type=").append(mediaType);
        }
        if (license != null) {
            apiUrl.append("&license=").append(license);
        }
        if (source != null) {
            apiUrl.append("&source=").append(source);
        }

        return restTemplate.getForObject(apiUrl.toString(), String.class);
    }
}
