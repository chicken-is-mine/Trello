package com.sparta.trello.domain.card.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.trello.domain.card.dto.CardSummary;
import com.sparta.trello.domain.card.entity.QCard;
import com.sparta.trello.domain.card.entity.QWorker;
import com.sparta.trello.domain.card.entity.Worker;
import com.sparta.trello.domain.comment.entity.QComment;
import com.sparta.trello.domain.user.entity.QUser;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<CardSummary> findCardsSummaryByColumnId(Long columnId) {
        QCard card = QCard.card;
        QWorker worker = QWorker.worker;
        QUser user = QUser.user;

        return queryFactory
            .select(Projections.fields(CardSummary.class,
                card.cardId, card.cardName,
                Projections.bean(Worker.class, worker.workerId, user.username)))
            .from(card)
            .leftJoin(worker.user, user) // Worker 엔티티와 User 엔티티를 조인합니다.
            .where(card.column.columnId.eq(columnId))
            .fetch();
    }

}
