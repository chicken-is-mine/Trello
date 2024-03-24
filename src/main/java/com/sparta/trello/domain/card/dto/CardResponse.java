package com.sparta.trello.domain.card.dto;

import com.sparta.trello.domain.card.entity.Card;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CardResponse {

    private String cardName;
    private String description;
    private Long sequence;
    private String color;
    private LocalDateTime dueDate;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    public CardResponse(Card card) {
        this.cardName = card.getCardName();
        this.description = card.getDescription();
        this.sequence = card.getSequence();
        this.color = card.getColor();
        this.dueDate = card.getDueDate();
        this.createAt = card.getCreatedAt();
        this.modifiedAt = card.getModifiedAt();
    }
}
