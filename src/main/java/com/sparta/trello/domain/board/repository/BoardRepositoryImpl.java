package com.sparta.trello.domain.board.repository;

import static com.sparta.trello.domain.board.entity.QBoard.board;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.trello.domain.board.dto.BoardInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class BoardRepositoryImpl implements CustomBoardRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoardInfo> findByUser_Id(Long userId) {
        return queryFactory
            .select(Projections.constructor(BoardInfo.class, board.boardId, board.boardName))
            .from(board)
            .where(board.user.id.eq(userId))
            .fetch();

    }
}
