package com.example.forum.service;

import com.example.forum.domain.Category;
import com.example.forum.domain.ForumThread;
import com.example.forum.domain.Reply;
import com.example.forum.domain.User;
import com.example.forum.repository.ReplyRepository;
import com.example.forum.repository.ThreadRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReplyServiceTest {
    @Mock
    private ThreadRepository threadRepository;
    @Mock
    private ReplyRepository replyRepository;

    @Test
    void requiresAuthenticationAndExistingThreadToReply() {
        UUID threadId = UUID.randomUUID();
        when(threadRepository.findById(threadId)).thenReturn(Optional.empty());
        User user = new User("Ana", "ana@email.com", "123");
        ReplyService service = new ReplyService(threadRepository, replyRepository);

        assertThrows(SecurityException.class, () -> service.create(null, threadId, "Resposta"));
        assertThrows(IllegalArgumentException.class, () -> service.create(user, threadId, "Resposta"));
        verifyNoInteractions(replyRepository);
    }

    @Test
    void listsRepliesChronologically() {
        User user = new User("Ana", "ana@email.com", "123");
        ForumThread thread = new ForumThread(user, new Category("Geral"), "Título", "Texto");
        Reply newer = new Reply(UUID.randomUUID(), thread.getId(), user, "Depois",
                Instant.parse("2026-01-02T00:00:00Z"));
        Reply older = new Reply(UUID.randomUUID(), thread.getId(), user, "Antes",
                Instant.parse("2026-01-01T00:00:00Z"));
        when(threadRepository.findById(thread.getId())).thenReturn(Optional.of(thread));
        when(replyRepository.findByThreadId(thread.getId())).thenReturn(List.of(newer, older));

        List<Reply> result = new ReplyService(threadRepository, replyRepository)
                .listByThread(thread.getId());

        assertEquals(List.of(older, newer), result);
    }
}
