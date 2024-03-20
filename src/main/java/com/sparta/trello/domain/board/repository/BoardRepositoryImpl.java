package com.sparta.trello.domain.board.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BoardRepositoryImpl implements BoardRepository {

  private final BoardJpaRepository boardJpaRepository;
  private final JPAQueryFactory jpaQueryFactory;

}
