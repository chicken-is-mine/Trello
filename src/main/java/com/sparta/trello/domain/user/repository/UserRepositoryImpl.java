package com.sparta.trello.domain.user.repository;

import static com.sparta.trello.domain.user.entity.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.trello.domain.user.entity.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements CustomUserRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<User> findByEmail(String email) {
        User foundUser = queryFactory
            .selectFrom(user)
            .where(user.email.eq(email)) // email 필드와 비교해야 합니다.
            .fetchOne();

        return Optional.ofNullable(foundUser);
    }
}
