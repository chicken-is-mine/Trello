package com.sparta.trello.domain.user.controller;

import com.sparta.trello.domain.user.dto.SignupRequest;
import com.sparta.trello.domain.user.dto.SignupResponse;
import com.sparta.trello.domain.user.service.UserService;
import com.sparta.trello.global.dto.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Controller
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public CommonResponse signup(@Valid @RequestBody SignupRequest signupRequest) {
        userService.signup(signupRequest);

       return CommonResponse.builder()
            .httpCode(201)
            .build();
    }

//    @PatchMapping("{userId}")
//    public CommonResponse updateUser
}
