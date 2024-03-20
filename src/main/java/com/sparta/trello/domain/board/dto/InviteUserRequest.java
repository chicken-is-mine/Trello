package com.sparta.trello.domain.board.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InviteUserRequest {
    private List<Long> userIds;
}