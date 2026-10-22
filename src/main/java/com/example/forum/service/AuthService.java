package com.example.forum.service;

import com.example.forum.domain.User;
import com.example.forum.repository.UserRepository;
import java.util.Objects;

public final class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository);
    }

    public User register(String username, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("email already registered");
        }
        return userRepository.save(new User(username, email, password));
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("invalid credentials"));
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("invalid credentials");
        }
        return user;
    }
}
