package com.example.forum.service;

import com.example.forum.domain.User;
import com.example.forum.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Test
    void registersWhenEmailIsAvailable() {
        when(userRepository.findByEmail("ana@email.com")).thenReturn(Optional.empty());
        User saved = new User("Ana", "ana@email.com", "123");
        when(userRepository.save(any(User.class))).thenReturn(saved);

        User result = new AuthService(userRepository)
                .register("Ana", "ana@email.com", "123");

        assertEquals(saved, result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void rejectsDuplicatedEmail() {
        when(userRepository.findByEmail("ana@email.com"))
                .thenReturn(Optional.of(new User("Ana", "ana@email.com", "123")));

        assertThrows(IllegalArgumentException.class,
                () -> new AuthService(userRepository)
                        .register("Outra", "ana@email.com", "456"));
        verify(userRepository, never()).save(any());
    }

    @Test
    void rejectsWrongPassword() {
        when(userRepository.findByEmail("ana@email.com"))
                .thenReturn(Optional.of(new User("Ana", "ana@email.com", "123")));

        assertThrows(IllegalArgumentException.class,
                () -> new AuthService(userRepository)
                        .login("ana@email.com", "errada"));
    }
}
