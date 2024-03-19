package com.sparta.trello.domain.user.dto;

import com.sparta.trello.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class SignupResponse {

    private Long id;
    private String email;
    private String username;
    private String profile;

    public SignupResponse(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.profile = user.getProfile();
    }

}
