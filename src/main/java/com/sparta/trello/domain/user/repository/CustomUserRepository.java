package com.sparta.trello.domain.user.repository;

import com.sparta.trello.domain.user.entity.User;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomUserRepository {

    Optional<User> findByEmail(String email);
}
