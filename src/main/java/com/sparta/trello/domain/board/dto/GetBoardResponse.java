package com.sparta.trello.domain.board.dto;

import com.sparta.trello.domain.board.entity.BoardColorEnum;
import com.sparta.trello.domain.card.dto.CardResponse;
import com.sparta.trello.domain.column.dto.GetColumnResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetBoardResponse {

  private Long boardId;
  private String boardName;
  private String description;
  private BoardColorEnum color;
  private List<GetColumnResponse> columns;
  private List<CardResponse> cards;

}
