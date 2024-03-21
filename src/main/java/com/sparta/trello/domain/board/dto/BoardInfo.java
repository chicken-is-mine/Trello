package com.sparta.trello.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardInfo {

    private Long boardId;
    private String boardName;
}
