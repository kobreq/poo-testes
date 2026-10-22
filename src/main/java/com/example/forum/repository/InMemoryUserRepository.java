package com.example.forum.repository;

import com.example.forum.domain.User;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryUserRepository implements UserRepository {
    private final Map<String, User> usersByEmail = new ConcurrentHashMap<>();

    @Override
    public User save(User user) {
        usersByEmail.put(user.getEmail(), user);
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(usersByEmail.get(email));
    }
}
