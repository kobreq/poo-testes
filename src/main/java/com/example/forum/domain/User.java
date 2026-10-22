package com.example.forum.domain;

import java.util.Objects;
import java.util.UUID;

public final class User {
    private final UUID id;
    private final String username;
    private final String email;
    private final String password;

    public User(String username, String email, String password) {
        this(UUID.randomUUID(), username, email, password);
    }

    public User(UUID id, String username, String email, String password) {
        this.id = Objects.requireNonNull(id);
        this.username = requireText(username, "username");
        this.email = requireText(email, "email");
        this.password = requireText(password, "password");
    }

    public UUID getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " cannot be blank");
        }
        return value.trim();
    }
}
