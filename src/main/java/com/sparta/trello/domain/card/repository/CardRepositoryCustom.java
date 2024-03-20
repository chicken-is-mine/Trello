package com.sparta.trello.domain.card.repository;

import com.sparta.trello.domain.card.dto.CardDetails;
import com.sparta.trello.domain.card.dto.CardSummary;
import java.util.List;

public interface CardRepositoryCustom {
    List<CardSummary> findCardsSummaryByColumnId(Long columnId);

//    List<CardDetails> findCardDetailsByColumnId(Long columnId, Long cardId);
}
