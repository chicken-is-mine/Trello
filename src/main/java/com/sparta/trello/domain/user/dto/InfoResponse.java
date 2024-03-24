package com.sparta.trello.domain.user.dto;

import com.sparta.trello.domain.board.dto.BoardInfo;
import com.sparta.trello.domain.card.dto.CardInfo;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InfoResponse {

    private String username;
    private String profile;
    private List<BoardInfo> boards;
    private List<CardInfo> cards;
}
