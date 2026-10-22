package com.example.forum.service;

import com.example.forum.domain.Category;
import com.example.forum.domain.ForumThread;
import com.example.forum.domain.User;
import com.example.forum.repository.ThreadRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class ThreadService {
    private final ThreadRepository threadRepository;

    public ThreadService(ThreadRepository threadRepository) {
        this.threadRepository = Objects.requireNonNull(threadRepository);
    }

    public ForumThread create(User authenticatedUser, Category category,
                              String title, String content) {
        requireAuthenticated(authenticatedUser);
        return threadRepository.save(new ForumThread(authenticatedUser, category, title, content));
    }

    public List<ForumThread> list() {
        return threadRepository.findAll().stream()
                .sorted(Comparator.comparing(ForumThread::getCreatedAt).reversed())
                .toList();
    }

    public void edit(User authenticatedUser, UUID threadId, String title, String content) {
        requireAuthenticated(authenticatedUser);
        ForumThread thread = find(threadId);
        if (!thread.getAuthor().getId().equals(authenticatedUser.getId())) {
            throw new SecurityException("only the author can edit the thread");
        }
        thread.edit(title, content);
        threadRepository.save(thread);
    }

    public void delete(User authenticatedUser, UUID threadId) {
        requireAuthenticated(authenticatedUser);
        ForumThread thread = find(threadId);
        if (!thread.getAuthor().getId().equals(authenticatedUser.getId())) {
            throw new SecurityException("only the author can delete the thread");
        }
        threadRepository.deleteById(threadId);
    }

    private ForumThread find(UUID threadId) {
        return threadRepository.findById(threadId)
                .orElseThrow(() -> new IllegalArgumentException("thread not found"));
    }

    private static void requireAuthenticated(User user) {
        if (user == null) {
            throw new SecurityException("authentication required");
        }
    }
}
