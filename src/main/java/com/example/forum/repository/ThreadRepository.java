package com.example.forum.repository;

import com.example.forum.domain.ForumThread;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ThreadRepository {
    ForumThread save(ForumThread thread);
    Optional<ForumThread> findById(UUID id);
    List<ForumThread> findAll();
    void deleteById(UUID id);
}
