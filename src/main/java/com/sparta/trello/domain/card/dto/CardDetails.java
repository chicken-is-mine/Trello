package com.sparta.trello.domain.card.dto;

import com.sparta.trello.domain.card.entity.Worker;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CardDetails {
    private Long cardId;
    private String cardName;
    private String description;
    private String color;
    private LocalDateTime dueDate;
    private List<Worker> workers;
}