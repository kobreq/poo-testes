package com.example.forum.service;

import com.example.forum.domain.Category;
import com.example.forum.domain.ForumThread;
import com.example.forum.domain.User;
import com.example.forum.repository.ThreadRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ThreadServiceTest {
    @Mock
    private ThreadRepository threadRepository;

    private final User author = new User("Ana", "ana@email.com", "123");
    private final Category category = new Category("Tecnologia");

    @Test
    void requiresAuthenticationToCreateThread() {
        assertThrows(SecurityException.class,
                () -> new ThreadService(threadRepository)
                        .create(null, category, "Título", "Conteúdo"));
        verifyNoInteractions(threadRepository);
    }

    @Test
    void onlyAuthorCanEditThread() {
        User other = new User("Bia", "bia@email.com", "123");
        ForumThread thread = new ForumThread(author, category, "Título", "Conteúdo");
        when(threadRepository.findById(thread.getId())).thenReturn(Optional.of(thread));

        assertThrows(SecurityException.class,
                () -> new ThreadService(threadRepository)
                        .edit(other, thread.getId(), "Novo", "Texto"));
        assertEquals("Título", thread.getTitle());
        verify(threadRepository, never()).save(any());
    }

    @Test
    void listsThreadsFromNewestToOldest() {
        ForumThread first = new ForumThread(author, category, "Primeiro", "Texto");
        ForumThread second = new ForumThread(author, category, "Segundo", "Texto");
        when(threadRepository.findAll()).thenReturn(List.of(first, second));

        List<ForumThread> result = new ThreadService(threadRepository).list();

        assertEquals(2, result.size());
        assertTrue(result.get(0).getCreatedAt().compareTo(result.get(1).getCreatedAt()) >= 0);
    }

    @Test
    void reportsMissingThreadOnDelete() {
        UUID id = UUID.randomUUID();
        when(threadRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> new ThreadService(threadRepository).delete(author, id));
        verify(threadRepository, never()).deleteById(any());
    }
}
