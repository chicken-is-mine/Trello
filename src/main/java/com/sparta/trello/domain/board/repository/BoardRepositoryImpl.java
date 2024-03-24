package com.sparta.trello.domain.board.repository;


import static com.sparta.trello.domain.board.entity.QBoard.board;
import static com.sparta.trello.domain.board.entity.QBoardUser.boardUser;
import static com.sparta.trello.domain.card.entity.QCard.card;
import static com.sparta.trello.domain.column.entity.QColumns.columns;
import static com.sparta.trello.domain.comment.entity.QComment.comment;
import static com.sparta.trello.domain.user.entity.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.trello.domain.board.dto.BoardInfo;
import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.comment.entity.Comment;
import com.sparta.trello.domain.comment.entity.QComment;
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
        return queryFactory
            .select(user)
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

    @Override
    public List<BoardInfo> findByUser_Id(Long userId) {
        return queryFactory
            .select(Projections.constructor(BoardInfo.class, board.boardId, board.boardName))
            .from(board)
            .where(board.user.id.eq(userId))
            .fetch();

    }

    @Override
    public void deleteBoardAndRelateEntities(Long boardId) {
        queryFactory.delete(boardUser)
            .where(boardUser.board.boardId.eq(boardId))
            .execute();

        queryFactory.delete(comment)
            .where(comment.card.cardId.eq(card.cardId))
            .execute();

        queryFactory.delete(card)
            .where(card.column.board.boardId.eq(boardId))
            .execute();

        queryFactory.delete(columns)
            .where(columns.board.boardId.eq(boardId))
            .execute();

        queryFactory.delete(board)
            .where(board.boardId.eq(boardId))
            .execute();
    }

}
