package com.sparta.trello.domain.card.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.trello.domain.card.dto.CardInfo;
import com.sparta.trello.domain.card.dto.CardSummary;
import com.sparta.trello.domain.card.entity.QCard;
import com.sparta.trello.domain.card.entity.QWorker;
import com.sparta.trello.domain.card.entity.Worker;
import com.sparta.trello.domain.comment.entity.QComment;
import com.sparta.trello.global.config.QueryDslConfig;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepositoryCustom {

    private final QueryDslConfig queryDslConfig;
    private final JPAQueryFactory queryFactory;

    @Override
    public List<CardSummary> findCardsSummaryByColumnId(Long columnId) {
        QCard card = QCard.card;
        QWorker worker = QWorker.worker;
        QComment comment = QComment.comment;

        return queryDslConfig.jpaQueryFactory()
            .select(Projections.constructor(CardSummary.class,
                card.cardId,
                card.cardName,
                Projections.list(Projections.fields(Worker.class, worker.user).as("workers")),
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
        QCard card = QCard.card;
        return queryFactory
            .select(Projections.constructor(CardInfo.class, card.cardId, card.cardName))
            .from(card)
            .where(card.user.id.eq(userId))
            .fetch();
    }

//    @Override
//    public List<CardDetails> findCardDetailsByColumnId(Long columnId, Long cardId) {
//        QCard card = QCard.card;
//        QWorker worker = QWorker.worker;
//        QUser user = QUser.user;
//        QComment comment = QComment.comment;
//
//        return queryDslConfig.jpaQueryFactory()
//            .select(Projections.constructor(CardDetails.class,
//                card.cardId,
//                card.cardName,
//                card.description,
//                card.color,
//                card.dueDate,
//                Projections.constructor(Worker.class, worker.user), // Worker 엔티티의 생성자 호출
//                Projections.bean(User.class, user.username),
//                Projections.bean(Comment.class, comment.commentId, comment.content)
//            ))
//            .from(card)
//            .leftJoin(card.workers, worker)
//            .leftJoin(card.comments, comment)
//            .where(card.column.columnId.eq(columnId))
//            .fetch();
//    }
}
