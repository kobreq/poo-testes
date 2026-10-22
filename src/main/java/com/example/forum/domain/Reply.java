package com.example.forum.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public record Reply(UUID id, UUID threadId, User author, String content, Instant createdAt) {
    public Reply {
        Objects.requireNonNull(id);
        Objects.requireNonNull(threadId);
        Objects.requireNonNull(author);
        Objects.requireNonNull(createdAt);
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("content cannot be blank");
        }
        content = content.trim();
    }

    public Reply(UUID threadId, User author, String content) {
        this(UUID.randomUUID(), threadId, author, content, Instant.now());
    }
}
