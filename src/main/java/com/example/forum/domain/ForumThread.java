package com.example.forum.domain;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public final class ForumThread {
    private final UUID id;
    private final User author;
    private final Category category;
    private String title;
    private String content;
    private final Instant createdAt;

    public ForumThread(User author, Category category, String title, String content) {
        this(UUID.randomUUID(), author, category, title, content, Instant.now());
    }

    public ForumThread(UUID id, User author, Category category, String title,
                       String content, Instant createdAt) {
        this.id = Objects.requireNonNull(id);
        this.author = Objects.requireNonNull(author);
        this.category = Objects.requireNonNull(category);
        this.title = requireText(title, "title");
        this.content = requireText(content, "content");
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    public void edit(String title, String content) {
        this.title = requireText(title, "title");
        this.content = requireText(content, "content");
    }

    public UUID getId() { return id; }
    public User getAuthor() { return author; }
    public Category getCategory() { return category; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public Instant getCreatedAt() { return createdAt; }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " cannot be blank");
        }
        return value.trim();
    }
}
