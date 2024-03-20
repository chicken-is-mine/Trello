package com.sparta.trello.domain.card.dto;

import com.sparta.trello.domain.card.entity.Worker;
import com.sparta.trello.domain.comment.entity.Comment;
import java.time.LocalDate;
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
    private String Description;
    private String color;
    private LocalDate dueDate;
    private List<Worker> workers;
    private List<Comment> comments;
}
