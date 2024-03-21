package com.sparta.trello.domain.board.controller;

import com.sparta.trello.domain.board.dto.BoardRequest;
import com.sparta.trello.domain.board.dto.BoardResponse;
import com.sparta.trello.domain.board.dto.GetBoardResponse;
import com.sparta.trello.domain.board.dto.InviteUserRequest;
import com.sparta.trello.domain.board.service.BoardService;
import com.sparta.trello.global.dto.CommonResponse;
import com.sparta.trello.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Board API", description = "워크스페이스 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards")
public class BoardController {

  private final BoardService boardService;

  @Operation(summary = "워크스페이스 생성", description = "입력된 boardName,description,color로 워크스페이스를 생성합니다.")
  @PostMapping
  public ResponseEntity<CommonResponse<BoardResponse>> createBoard(
      @Valid @RequestBody BoardRequest boardRequest,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    BoardResponse boardResponse = boardService.createBoard(boardRequest, userDetails.getUser());
    return ResponseEntity.status(HttpStatus.CREATED).body(
        CommonResponse.<BoardResponse>builder()
            .httpCode(HttpStatus.CREATED.value())
            .data(boardResponse).build()
    );
  }


  @Operation(summary = "워크스페이스 수정", description = "워크스페이스 Owner가 워크스페이스 정보를 수정합니다.")
  @PatchMapping("/{boardId}")
  public ResponseEntity<CommonResponse<BoardResponse>> updateBoard(
      @PathVariable Long boardId,
      @Valid @RequestBody BoardRequest boardRequest,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    BoardResponse boardResponse = boardService.updateBoard(boardId, boardRequest,
        userDetails.getUser().getId());
    return ResponseEntity.ok(
        CommonResponse.<BoardResponse>builder()
            .httpCode(HttpStatus.OK.value())
            .data(boardResponse)
            .build()
    );
  }

  @Operation(summary = "워크스페이스 삭제", description = "워크스페이스 Owner가 워크스페이스를 삭제합니다")
  @DeleteMapping("/{boardId}")
  public ResponseEntity<CommonResponse<String>> deleteBoard(
      @PathVariable Long boardId,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    boardService.deleteBoard(boardId, userDetails.getUser().getId());
    return ResponseEntity.ok(
        CommonResponse.<String>builder()
            .httpCode(HttpStatus.OK.value())
            .data("삭제가 완료되었습니다.")
            .build()
    );
  }

  @Operation(summary = "워크스페이스 초대", description = "워크스페이스 Owner가 유저를 초대합니다.")
  @PostMapping("/{boardId}/invite")
  public ResponseEntity<CommonResponse<String>> inviteUser(
      @PathVariable Long boardId,
      @RequestBody InviteUserRequest inviteUserRequest,
      @AuthenticationPrincipal UserDetailsImpl userDetails
  ) {
    boardService.inviteUser(boardId, inviteUserRequest.getUserIds(), userDetails.getUser().getId());
    return ResponseEntity.ok(
        CommonResponse.<String>builder()
            .httpCode(HttpStatus.OK.value())
            .data("초대가 완료되었습니다.")
            .build()
    );
  }

  @Operation(summary = "워크스페이스 조회", description = "워크스페이스에 속한 Owner,User가 워크스페이스 정보를 조회합니다.")
  @GetMapping("/{boardId}")
  public ResponseEntity<CommonResponse<GetBoardResponse>> getBoardList(@PathVariable Long boardId,
      @AuthenticationPrincipal UserDetailsImpl userDetails) {
    GetBoardResponse boardResponse = boardService.getBoardList(boardId, userDetails.getUser());
    return ResponseEntity.ok(
        CommonResponse.<GetBoardResponse>builder()
            .httpCode(HttpStatus.OK.value())
            .data(boardResponse)
            .build()
    );
  }

}
