package com.sparta.trello.domain.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CardInfo {

    private Long cardId;
    private String cardName;
}
