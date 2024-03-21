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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<CommonResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {
        userService.signup(signupRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(
            CommonResponse.builder()
                .httpCode(HttpStatus.CREATED.value())
                .data(signupRequest.getUsername()+"의 회원가입이 완료되었습니다.")
                .build());
    }

    @GetMapping()
    public ResponseEntity<CommonResponse<Object>> showProfile(
        @AuthenticationPrincipal UserDetailsImpl userDetails){
        InfoResponse response = userService.showProfile(userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(
            CommonResponse.builder()
                .httpCode(HttpStatus.OK.value())
                .data(response)
                .build());
    }
    @PatchMapping()
    public ResponseEntity<CommonResponse> updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody @Valid InfoRequest profileRequest){
        userService.updateProfile(userDetails.getUser(),profileRequest);

        return ResponseEntity.status(HttpStatus.OK).body(
            CommonResponse.builder()
                .httpCode(HttpStatus.OK.value())
                .data("회원정보가 수정되었습니다.")
                .build());
    }


}
