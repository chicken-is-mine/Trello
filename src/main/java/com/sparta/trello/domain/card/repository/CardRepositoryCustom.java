package com.sparta.trello.domain.card.repository;

import com.sparta.trello.domain.card.dto.CardDetails;
import com.sparta.trello.domain.card.dto.CardInfo;
import com.sparta.trello.domain.card.dto.CardSummary;
import com.sparta.trello.domain.card.entity.Card;
import java.util.List;

public interface CardRepositoryCustom {
    List<CardSummary> findCardsSummaryByColumnId(Long columnId);

    List<CardDetails> findCardDetailsByColumnId(Long columnId, Long cardId);

    Card findBySequence(Long columnId, Long prevSequence);

    List<CardInfo> findByUser_Id(Long userId);

    List<Card> findAllByBoardId(Long boardId);

//    List<CardDetails> findCardDetailsByColumnId(Long columnId, Long cardId);
}
