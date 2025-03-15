package com.mediaapp.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SearchHistoryDTO {
    private String searchQuery;
    private LocalDateTime timestamp;
}
