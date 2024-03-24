package com.sparta.trello.domain.card.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CardUpdateRequest {

    private String cardName;
    private String description;
    private String color;
    private Long workerId;
    private Long removeWorkerId;
    private LocalDateTime dueDate;
}
