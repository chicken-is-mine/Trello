package com.sparta.trello.domain.comment.dto;

import com.sparta.trello.domain.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CommentResponse {

    private String userName;
    private String content;

    public CommentResponse(Comment comment) {
        this.userName = comment.getUser().getUsername();
        this.content = comment.getContent();
    }
}
