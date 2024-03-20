package com.sparta.trello.domain.user.controller;

import com.sparta.trello.domain.user.dto.SignupRequest;
import com.sparta.trello.domain.user.service.UserService;
import com.sparta.trello.global.dto.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
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
