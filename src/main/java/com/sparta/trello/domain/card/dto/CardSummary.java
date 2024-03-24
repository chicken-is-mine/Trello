package com.sparta.trello.domain.card.dto;

import com.sparta.trello.domain.card.entity.Worker;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CardSummary {

    private Long cardId;
    private String cardName;
    private List<Worker> workers;
    private Long commentCount;
}
