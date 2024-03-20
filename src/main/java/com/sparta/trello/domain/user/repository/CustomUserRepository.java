package com.sparta.trello.domain.user.repository;

import com.sparta.trello.domain.user.entity.User;
import java.util.Optional;

public interface CustomUserRepository {
    Optional<User> findByEmail(String email);
}
