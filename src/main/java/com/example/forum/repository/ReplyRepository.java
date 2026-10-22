package com.example.forum.repository;

import com.example.forum.domain.Reply;
import java.util.List;
import java.util.UUID;

public interface ReplyRepository {
    Reply save(Reply reply);
    List<Reply> findByThreadId(UUID threadId);
}
