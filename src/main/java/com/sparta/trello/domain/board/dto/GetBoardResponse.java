package com.sparta.trello.domain.board.dto;

import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.entity.BoardColorEnum;
import com.sparta.trello.domain.card.dto.CardResponse;
import com.sparta.trello.domain.column.dto.GetColumnResponse;
import java.util.List;
import lombok.Getter;

@Getter
public class GetBoardResponse {

  private Long boardId;
  private String boardName;
  private String description;
  private BoardColorEnum color;
  private List<GetColumnResponse> columns;
  private List<CardResponse> cards;

  public GetBoardResponse(Board board, List<GetColumnResponse> columns, List<CardResponse> cards) {
    this.boardId = board.getBoardId();
    this.boardName = board.getBoardName();
    this.description = board.getDescription();
    this.color = board.getColor();
    this.columns = columns;
    this.cards = cards;
  }

  public GetBoardResponse(Long boardId, String boardName, String description, BoardColorEnum color,
      List<GetColumnResponse> columns, List<CardResponse> cards) {
    this.boardId = boardId;
    this.boardName = boardName;
    this.description = description;
    this.color = color;
    this.columns = columns;
    this.cards = cards;
  }

}
