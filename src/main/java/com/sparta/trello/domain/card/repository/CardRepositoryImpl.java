package com.sparta.trello.domain.card.repository;

import static com.sparta.trello.domain.card.entity.QCard.card;
import static com.sparta.trello.domain.card.entity.QWorker.worker;
import static com.sparta.trello.domain.comment.entity.QComment.comment;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.trello.domain.card.dto.CardDetails;
import com.sparta.trello.domain.card.dto.CardInfo;
import com.sparta.trello.domain.card.dto.CardSummary;
import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.card.entity.Worker;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;


    @Override
    public List<CardSummary> findCardsSummaryByColumnId(Long columnId) {
        List<Tuple> results = queryFactory
            .select(card.cardId, card.cardName, worker.user.username, comment.count())
            .from(card)
            .leftJoin(card.workers, worker)
            .leftJoin(comment).on(card.eq(comment.card))
            .leftJoin(worker.user)
            .where(card.column.columnId.eq(columnId))
            .groupBy(card.cardId, card.cardName, worker.user.username)
            .fetch();

        Map<Long, CardSummary> cardSummaryMap = new LinkedHashMap<>();

        for (Tuple tuple : results) {
            Long cardId = tuple.get(card.cardId);
            String cardName = tuple.get(card.cardName);
            String workerUsername = tuple.get(worker.user.username);
            Long commentCount = tuple.get(comment.count());

            if (!cardSummaryMap.containsKey(cardId)) {
                CardSummary cardSummary = new CardSummary(cardId, cardName, new ArrayList<>(),
                    commentCount);
                cardSummaryMap.put(cardId, cardSummary);
            }

            CardSummary cardSummary = cardSummaryMap.get(cardId);
            if (workerUsername != null) {
                Worker worker = new Worker(workerUsername);
                cardSummary.getWorkers().add(worker);
            }
        }

        return new ArrayList<>(cardSummaryMap.values());
    }

//        return queryFactory
//            .select(Projections.constructor(CardSummary.class,
//                card.cardId,
//                card.cardName,
//                Projections.list(
//                    Projections.fields(Worker.class, worker.user.username).as("workers")),
//                comment.count()))
//            .from(card)
//            .leftJoin(card.workers, worker)
//            .leftJoin(comment).on(card.eq(comment.card))
//            .leftJoin(worker.user) // 추가
//            .where(card.column.columnId.eq(columnId))
//            .groupBy(card.cardId, card.cardName, worker.user) // 수정
//            .fetch();
//    }

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
        List<Tuple> results = queryFactory
            .select(card, worker.user.username)
            .from(card)
            .leftJoin(card.workers, worker)
            .where(card.column.columnId.eq(columnId).and(card.cardId.eq(cardId)))
            .fetch();

        Map<Long, CardDetails> cardDetailsMap = new LinkedHashMap<>();

        results.forEach(tuple -> {
            Card cardEntity = tuple.get(0, Card.class);
            String workerUsername = tuple.get(1, String.class);

            Long cardId1 = cardEntity.getCardId();

            cardDetailsMap.computeIfAbsent(cardId1, id ->
                new CardDetails(
                    cardEntity.getCardId(),
                    cardEntity.getCardName(),
                    cardEntity.getDescription(),
                    cardEntity.getColor(),
                    cardEntity.getDueDate(),
                    new ArrayList<>(),
                    new ArrayList<>()
                ));

            if (workerUsername != null) {
                cardDetailsMap.get(cardId1).getWorkers().add(new Worker(workerUsername));
            }
        });

        // If cardDetailsMap doesn't contain the requested cardId, retrieve data from the database
        if (!cardDetailsMap.containsKey(cardId)) {
            // Fetch card details directly from the database
            Card cardEntity = entityManager.find(Card.class, cardId);

            if (cardEntity != null) {
                CardDetails cardDetails = new CardDetails(
                    cardEntity.getCardId(),
                    cardEntity.getCardName(),
                    cardEntity.getDescription(),
                    cardEntity.getColor(),
                    cardEntity.getDueDate(),
                    new ArrayList<>(),
                    new ArrayList<>()
                );

                cardDetailsMap.put(cardId, cardDetails);
            }
        }

        return new ArrayList<>(cardDetailsMap.values());
    }
//    @Override
//    public List<CardDetails> findCardDetailsByColumnId(Long columnId, Long cardId) {
//        List<Tuple> results = queryFactory
//            .select(card, worker.user.username, comment)
//            .from(card)
//            .leftJoin(card.workers, worker)
//            .leftJoin(comment).on(comment.card.eq(card))
//            .where(card.column.columnId.eq(columnId).and(card.cardId.eq(cardId)))
//            .fetch();
//
//        Map<Long, CardDetails> cardDetailsMap = new LinkedHashMap<>();
//
//        for (Tuple tuple : results) {
//            Card cardEntity = tuple.get(0, Card.class);
//            String workerUsername = tuple.get(1, String.class);
//            Comment commentEntity = tuple.get(2, Comment.class);
//
//            Long cardId1 = cardEntity.getCardId();
//
//            CardDetails cardDetails = cardDetailsMap.computeIfAbsent(cardId1, id ->
//                new CardDetails(
//                    cardEntity.getCardId(),
//                    cardEntity.getCardName(),
//                    cardEntity.getDescription(),
//                    cardEntity.getColor(),
//                    cardEntity.getDueDate(),
//                    new ArrayList<>(),
//                    new ArrayList<>()
//                ));
//
//            if (workerUsername != null) {
//                Worker worker = new Worker(workerUsername);
//                cardDetails.getWorkers().add(worker);
//            }
//
//            if (commentEntity != null) {
//                cardDetails.getComments().add(commentEntity);
//            }
//        }
//
//        return new ArrayList<>(cardDetailsMap.values());
//    }

//    @Override
//    public List<CardDetails> findCardDetailsByColumnId(Long columnId, Long cardId) {
//        return queryFactory
//            .select(Projections.constructor(CardDetails.class,
//                card.cardId,
//                card.cardName,
//                card.description,
//                card.color,
//                card.dueDate,
//                Projections.list(
//                    Projections.fields(Worker.class, worker.user.username).as("workers")
//                ),
//                Projections.list(
//                    Projections.fields(Comment.class, comment).as("comments")
//                )
//            ))
//            .from(card)
//            .leftJoin(card.workers, worker)
//            .leftJoin(comment).on(comment.card.eq(card)) // comment 와 card 연관 관계 설정
//            .where(card.column.columnId.eq(columnId)
//                .and(card.cardId.eq(cardId)))
//            .groupBy(card.cardId, card.cardName, card.description, card.color, card.dueDate, worker.user.username, comment)
//            .fetch();
//    }


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
}