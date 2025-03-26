package com.mediaapp.model;

import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)  // ✅ Lazy loading for efficiency
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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
