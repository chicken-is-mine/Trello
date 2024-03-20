package com.sparta.trello.domain.user.controller;

import com.sparta.trello.domain.user.dto.InfoRequest;
import com.sparta.trello.domain.user.dto.InfoResponse;
import com.sparta.trello.domain.user.dto.SignupRequest;
import com.sparta.trello.domain.user.service.UserService;
import com.sparta.trello.global.dto.CommonResponse;
import com.sparta.trello.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @GetMapping()
    public CommonResponse<Object> showProfile(@AuthenticationPrincipal UserDetailsImpl userDetails){
        InfoResponse response = userService.showProfile(userDetails.getUser());

        return CommonResponse.builder()
            .httpCode(201)
            .data(response)
            .build();
    }
    @PatchMapping()
    public CommonResponse<Object> updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid InfoRequest profileRequest){
        userService.updateProfile(userDetails.getUser(),profileRequest);

        return CommonResponse.builder()
            .httpCode(204)
            .build();
    }


}
