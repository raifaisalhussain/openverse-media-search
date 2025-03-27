package com.mediaapp.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "search_history")
@Getter
@Setter
@NoArgsConstructor
public class SearchHistory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User is required")
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotBlank(message = "Search query must not be blank")
    @Size(min = 1, max = 255, message = "Search query must be between 1 and 255 characters")
    @Column(nullable = false)
    private String searchQuery;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    public SearchHistory(User user, String searchQuery) {
        this.user = user;
        this.searchQuery = searchQuery;
        this.timestamp = LocalDateTime.now();
    }
}
