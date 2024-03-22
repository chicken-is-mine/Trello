package com.sparta.trello.domain.column.repository;

import static com.sparta.trello.domain.column.entity.QColumns.columns;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.trello.domain.column.entity.Columns;
import java.util.List;
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
            .where(columns.sequence.eq(sequence), columns.board.boardId.eq(boardId))
            .fetchFirst();
    }

    @Override
    public List<Columns> findAllByBoardIdOrderBySequence(Long boardId) {
        return queryFactory
            .selectFrom(columns)
            .where(columns.board.boardId.eq(boardId))
            .orderBy(columns.sequence.asc())
            .fetch();
    }
}
