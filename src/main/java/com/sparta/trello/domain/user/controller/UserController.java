package com.sparta.trello.domain.user.controller;

import com.sparta.trello.domain.user.dto.InfoRequest;
import com.sparta.trello.domain.user.dto.InfoResponse;
import com.sparta.trello.domain.user.dto.SignupRequest;
import com.sparta.trello.domain.user.service.UserService;
import com.sparta.trello.global.dto.CommonResponse;
import com.sparta.trello.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User API", description = "유저 API")
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "회원가입")
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse> signup(@Valid @RequestBody SignupRequest signupRequest) {
        userService.signup(signupRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(
            CommonResponse.builder()
                .httpCode(HttpStatus.CREATED.value())
                .data(signupRequest.getUsername() + "의 회원가입이 완료되었습니다.")
                .build());
    }

    @Operation(summary = "마이페이지 조회", description = "사용자가 초대된 board와 작성한 card가 있는 마이페이지 조회입니다. ")
    @GetMapping()
    public ResponseEntity<CommonResponse<Object>> showProfile(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        InfoResponse response = userService.showProfile(userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(
            CommonResponse.builder()
                .httpCode(HttpStatus.OK.value())
                .data(response)
                .build());
    }

    @Operation(summary = "사용자 정보 수정", description = "사용자의 닉네임과 한줄소개를 수정합니다.")
    @PatchMapping()
    public ResponseEntity<CommonResponse> updateProfile(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody @Valid InfoRequest profileRequest) {
        userService.updateProfile(userDetails.getUser(), profileRequest);

        return ResponseEntity.status(HttpStatus.OK).body(
            CommonResponse.builder()
                .httpCode(HttpStatus.OK.value())
                .data("회원정보가 수정되었습니다.")
                .build());
    }
}
