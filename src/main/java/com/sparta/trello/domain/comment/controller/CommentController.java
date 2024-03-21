package com.sparta.trello.domain.comment.controller;

import com.sparta.trello.domain.comment.dto.CommentRequest;
import com.sparta.trello.domain.comment.service.CommentService;
import com.sparta.trello.global.dto.CommonResponse;
import com.sparta.trello.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards/{boardId}/cards/{cardId}/comments")
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping()
    public ResponseEntity<CommonResponse> createComment(@PathVariable Long cardId,
        @PathVariable Long boardId,
        @RequestBody CommentRequest request,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.createComment(cardId, boardId, request, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(
            CommonResponse.builder()
                .httpCode(HttpStatus.CREATED.value())
                .data("댓글이 생성되었습니다")
                .build());

    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommonResponse> updateComment(@PathVariable Long commentId, @PathVariable Long boardId,
        @RequestBody CommentRequest request,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.updateComment(commentId, boardId, request, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(
            CommonResponse.builder()
                .httpCode(HttpStatus.OK.value())
                .data("댓글이 수정되었습니다")
                .build());
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommonResponse> deleteComment(@PathVariable Long commentId, @PathVariable Long boardId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        commentService.deleteComment(commentId, boardId, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK).body(
            CommonResponse.builder()
                .httpCode(HttpStatus.OK.value())
                .data("댓글이 삭제되었습니다")
                .build());
    }


}
