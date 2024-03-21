package com.sparta.trello.domain.board.repository;


import static com.sparta.trello.domain.board.entity.QBoardUser.boardUser;
import static com.sparta.trello.domain.user.entity.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.user.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BoardRepositoryImpl implements CustomBoardRepository {


  private final JPAQueryFactory queryFactory;

  @Override
  public List<User> findExistingMemberByBoard(Board board) {
    return queryFactory.select(user)
        .from(boardUser)
        .join(boardUser.user, user)
        .where(boardUser.board.eq(board))
        .fetch();
  }

  @Override
  public List<User> findUsersByIds(List<Long> userIds) {
    return queryFactory.selectFrom(user)
        .where(user.id.in(userIds))
        .fetch();
  }


}
