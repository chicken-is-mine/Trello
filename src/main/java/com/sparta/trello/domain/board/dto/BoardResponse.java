package com.sparta.trello.domain.board.dto;

import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.entity.BoardColorEnum;
import lombok.Getter;

@Getter
public class BoardResponse {

    private String boardName;
    private String description;
    private BoardColorEnum color;

    public BoardResponse(Board board) {
        this.boardName = board.getBoardName();
        this.description = board.getDescription();
        this.color = board.getColor();
    }
}