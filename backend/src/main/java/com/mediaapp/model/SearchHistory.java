package com.mediaapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "search_history")
@Getter
@Setter  // ✅ Lombok auto-generates getters and setters
public class SearchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String searchQuery;

    private LocalDateTime timestamp = LocalDateTime.now();
}
