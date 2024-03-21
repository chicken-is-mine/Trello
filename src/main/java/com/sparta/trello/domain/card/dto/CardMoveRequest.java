package com.sparta.trello.domain.card.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardMoveRequest {
    private Long prevSequence;
    private Long nextSequence;
}
