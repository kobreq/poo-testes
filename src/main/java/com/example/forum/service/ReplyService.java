package com.example.forum.service;

import com.example.forum.domain.ForumThread;
import com.example.forum.domain.Reply;
import com.example.forum.domain.User;
import com.example.forum.repository.ReplyRepository;
import com.example.forum.repository.ThreadRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class ReplyService {
    private final ThreadRepository threadRepository;
    private final ReplyRepository replyRepository;

    public ReplyService(ThreadRepository threadRepository, ReplyRepository replyRepository) {
        this.threadRepository = Objects.requireNonNull(threadRepository);
        this.replyRepository = Objects.requireNonNull(replyRepository);
    }

    public Reply create(User authenticatedUser, UUID threadId, String content) {
        requireAuthenticated(authenticatedUser);
        findThread(threadId);
        return replyRepository.save(new Reply(threadId, authenticatedUser, content));
    }

    public List<Reply> listByThread(UUID threadId) {
        findThread(threadId);
        return replyRepository.findByThreadId(threadId).stream()
                .sorted(Comparator.comparing(Reply::createdAt))
                .toList();
    }

    private ForumThread findThread(UUID threadId) {
        return threadRepository.findById(threadId)
                .orElseThrow(() -> new IllegalArgumentException("thread not found"));
    }

    private static void requireAuthenticated(User user) {
        if (user == null) {
            throw new SecurityException("authentication required");
        }
    }
}
