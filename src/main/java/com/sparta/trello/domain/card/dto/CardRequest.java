package com.sparta.trello.domain.card.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CardRequest {
    private String cardName;
    private int sequence;
}
