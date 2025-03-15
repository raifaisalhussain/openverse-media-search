package com.mediaapp.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING) // ✅ Ensure Enum is stored as String in DB
    @Column(nullable = false)
    private Role role;

    public enum Role {
        USER, ADMIN
    }
}
