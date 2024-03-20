package com.sparta.trello.domain.board.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class BoardRequest {

  @NotBlank(message = "제목을 입력하세요")
  private String boardName;
  @NotBlank(message = "설명을 입력하세요")
  private String description;
  @NotNull(message = "색을 선택하세요")
  private Integer color;

}