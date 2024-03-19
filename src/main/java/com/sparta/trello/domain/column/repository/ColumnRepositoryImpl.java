package com.sparta.trello.domain.column.repository;

import static com.sparta.trello.domain.board.entity.QBoard.board;
import static com.sparta.trello.domain.column.entity.QColumns.columns;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.trello.domain.column.entity.Columns;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ColumnRepositoryImpl implements CustomColumnRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Columns findBySequence(Long boardId, Long sequence) {
        return queryFactory
            .selectFrom(columns)
            .join(board)
            .where(columns.sequence.eq(sequence))
            .fetchFirst();
    }
}
