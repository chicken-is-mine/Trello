package com.sparta.trello.domain.user.repository;

import com.sparta.trello.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {

}
