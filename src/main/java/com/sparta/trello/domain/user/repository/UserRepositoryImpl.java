package com.sparta.trello.domain.user.repository;

import com.sparta.trello.domain.user.entity.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements CustomUserRepository{

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }
}
