package com.sparta.trello.domain.card.repository;

import com.querydsl.core.types.Projections;
import com.sparta.trello.domain.card.dto.CardDetails;
import com.sparta.trello.domain.card.dto.CardSummary;
import com.sparta.trello.domain.card.entity.QCard;
import com.sparta.trello.domain.card.entity.QWorker;
import com.sparta.trello.domain.card.entity.Worker;
import com.sparta.trello.domain.comment.entity.Comment;
import com.sparta.trello.domain.comment.entity.QComment;
import com.sparta.trello.domain.user.entity.QUser;
import com.sparta.trello.global.config.QueryDslConfig;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepositoryCustom {
    private final QueryDslConfig queryDslConfig;

    @Override
    public List<CardSummary> findCardsSummaryByColumnId(Long columnId) {
        QCard card = QCard.card;
        QWorker worker = QWorker.worker;
        QUser user = QUser.user;

        return queryDslConfig.jpaQueryFactory()
            .select(Projections.fields(CardSummary.class,
                card.cardId, card.cardName,
                Projections.bean(Worker.class, worker.workerId, user.username)))
            .from(card)
            .leftJoin(worker.user, user)
            .where(card.column.columnId.eq(columnId))
            .fetch();
    }

    @Override
    public List<CardDetails> findCardDetailsByColumnId(Long columnId) {
        QCard card = QCard.card;
        QWorker worker = QWorker.worker;
        QUser user = QUser.user;
        QComment comment = QComment.comment;

        return queryDslConfig.jpaQueryFactory()
            .select(Projections.fields(CardDetails.class,
                card.cardId,
                card.cardName,
                card.description,
                card.color,
                card.dueDate,
                Projections.bean(Worker.class,
                    worker.workerId,
                    user.username).as("workers"),
                Projections.bean(Comment.class,
                    comment.commentId,
                    comment.content).as("comments")))
            .from(card)
            .leftJoin(card.workers, worker).fetchJoin()
            .leftJoin(comment).on(card.eq(comment.card))
            .where(card.column.columnId.eq(columnId))
            .fetch();
    }
}
