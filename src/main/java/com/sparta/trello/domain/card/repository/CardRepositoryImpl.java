package com.sparta.trello.domain.card.repository;

import static com.sparta.trello.domain.card.entity.QCard.card;
import static com.sparta.trello.domain.card.entity.QWorker.worker;
import static com.sparta.trello.domain.comment.entity.QComment.comment;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.trello.domain.card.dto.CardDetails;
import com.sparta.trello.domain.card.dto.CardInfo;
import com.sparta.trello.domain.card.dto.CardSummary;
import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.card.entity.Worker;
import com.sparta.trello.domain.comment.entity.Comment;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public List<CardSummary> findCardsSummaryByColumnId(Long columnId) {

        return queryFactory
            .select(Projections.constructor(CardSummary.class,
                card.cardId,
                card.cardName,
                Projections.list(
                    Projections.fields(Worker.class, worker.user.username).as("workers")),
                comment.count()))
            .from(card)
            .leftJoin(card.workers, worker)
            .leftJoin(comment).on(card.eq(comment.card))
            .leftJoin(worker.user) // 추가
            .where(card.column.columnId.eq(columnId))
            .groupBy(card.cardId, card.cardName, worker.user) // 수정
            .fetch();
    }

    @Override
    public List<CardInfo> findByUser_Id(Long userId) {
        return queryFactory
            .select(Projections.constructor(CardInfo.class, card.cardId, card.cardName))
            .from(card)
            .where(card.user.id.eq(userId))
            .fetch();
    }

    @Override
    public List<CardDetails> findCardDetailsByColumnId(Long columnId, Long cardId) {
        return queryFactory
            .select(Projections.constructor(CardDetails.class,
                card.cardId,
                card.cardName,
                card.description,
                card.color,
                card.dueDate,
                Projections.list(
                    Projections.fields(Worker.class, worker.user.username).as("workers")
                ),
                Projections.list(
                    Projections.fields(Comment.class, comment).as("comments")
                )
            ))
            .from(card)
            .leftJoin(card.workers, worker)
            .leftJoin(comment).on(comment.card.eq(card)) // comment와 card 연관 관계 설정
            .where(card.column.columnId.eq(columnId)
                .and(card.cardId.eq(cardId)))
            .fetch();
    }


    @Override
    public Card findBySequence(Long columnId, Long sequence) {
        return queryFactory
            .selectFrom(card)
            .join(card.column)
            .where(card.sequence.eq(sequence), card.column.columnId.eq(columnId))
            .fetchFirst();
    }

    @Override
    public List<Card> findAllByBoardId(Long boardId) {
        return queryFactory
            .selectFrom(card)
            .join(card.column)
            .where(card.column.board.boardId.eq(boardId))
            .fetch();
    }

    private List<Worker> workerList() {
        return queryFactory
            .selectFrom(worker)
            .fetch();
    }

    private List<Comment> commentList() {
        return queryFactory
            .selectFrom(comment)
            .fetch();
    }
}