package com.sparta.trello.domain.card.repository;

import com.sparta.trello.domain.card.dto.CardInfo;
import com.sparta.trello.domain.card.dto.CardSummary;
import java.util.List;

public interface CardRepositoryCustom {

    List<CardSummary> findCardsSummaryByColumnId(Long columnId);

    List<CardInfo> findByUser_Id(Long userId);

//    List<CardDetails> findCardDetailsByColumnId(Long columnId, Long cardId);
}
