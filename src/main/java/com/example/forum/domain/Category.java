package com.example.forum.domain;

import java.util.Objects;
import java.util.UUID;

public record Category(UUID id, String name) {
    public Category {
        Objects.requireNonNull(id);
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be blank");
        }
        name = name.trim();
    }

    public Category(String name) {
        this(UUID.randomUUID(), name);
    }
}
