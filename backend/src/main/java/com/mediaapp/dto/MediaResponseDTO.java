package com.mediaapp.dto;

import lombok.Data;

@Data
public class MediaResponseDTO {
    private String title;
    private String imageUrl;
    private String sourceUrl;
    private String license;
}
