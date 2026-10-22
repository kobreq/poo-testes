package com.example.forum.repository;

import com.example.forum.domain.User;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findByEmail(String email);
}
