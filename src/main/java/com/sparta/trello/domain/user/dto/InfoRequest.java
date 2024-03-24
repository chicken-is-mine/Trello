package com.sparta.trello.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InfoRequest {

    private String username;
    private String profile;
}
