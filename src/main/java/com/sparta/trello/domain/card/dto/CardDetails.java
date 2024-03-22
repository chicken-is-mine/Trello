package com.sparta.trello.domain.card.dto;

import com.sparta.trello.domain.card.entity.Worker;
import com.sparta.trello.domain.comment.dto.CommentResponse;
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
    private List<CommentResponse> comments;

    public CardDetails(CardDetails cardDetails, List<CommentResponse> comments) {
        this.cardId = cardDetails.getCardId();
        this.cardName = cardDetails.getCardName();
        this.description = cardDetails.getDescription();
        this.color = cardDetails.getColor();
        this.dueDate = cardDetails.getDueDate();
        this.workers = cardDetails.getWorkers();
        this.comments = cardDetails.getComments();
        this.comments.addAll(comments);
    }
}