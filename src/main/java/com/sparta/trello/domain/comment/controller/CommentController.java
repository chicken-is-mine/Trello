package com.sparta.trello.domain.comment.controller;

import com.sparta.trello.domain.comment.dto.CommentRequest;
import com.sparta.trello.domain.comment.service.CommentService;
import com.sparta.trello.global.dto.CommonResponse;
import com.sparta.trello.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/v1/cards/{cardId}/comments")
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping()
    public CommonResponse createComment(@PathVariable Long cardId,@RequestBody CommentRequest request,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.createComment(cardId,request,userDetails.getUser());

        return CommonResponse.builder()
            .httpCode(201)
            .build();
    }
    @PatchMapping("/{commentId}")
    public CommonResponse updateComment(@PathVariable Long commentId,@RequestBody CommentRequest request,
        @AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.updateComment(commentId,request,userDetails.getUser());

        return CommonResponse.builder()
            .httpCode(204)
            .build();
    }
    @DeleteMapping("/{commentId}")
    public CommonResponse deleteComment(@PathVariable Long commentId,@AuthenticationPrincipal UserDetailsImpl userDetails){
        commentService.deleteComment(commentId, userDetails.getUser());

        return CommonResponse.builder()
            .httpCode(204)
            .build();
    }


}
