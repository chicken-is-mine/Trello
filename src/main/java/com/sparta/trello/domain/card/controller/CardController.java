package com.sparta.trello.domain.card.controller;

import com.sparta.trello.domain.card.dto.CardDetails;
import com.sparta.trello.domain.card.dto.CardMoveRequest;
import com.sparta.trello.domain.card.dto.CardRequest;
import com.sparta.trello.domain.card.dto.CardResponse;
import com.sparta.trello.domain.card.dto.CardSummary;
import com.sparta.trello.domain.card.dto.CardUpdateRequest;
import com.sparta.trello.domain.card.service.CardService;
import com.sparta.trello.global.dto.CommonResponse;
import com.sparta.trello.global.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Card API", description = "카드 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards/{boardId}/columns/{columnId}")
public class CardController {

    private final CardService cardService;

    @Operation(summary = "카드 생성", description = "입력된 columnId에 카드를 생성합니다.")
    @PostMapping("/cards")
    public ResponseEntity<CommonResponse<CardResponse>> createCard(
        @PathVariable Long boardId,
        @PathVariable Long columnId,
        @RequestBody CardRequest request,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        cardService.createCard(boardId, columnId, request, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).body(
            CommonResponse.<CardResponse>builder()
                .httpCode(HttpStatus.NO_CONTENT.value()).build());
    }

    @Operation(summary = "카드 요약 정보 조회", description = "입력받은 columnId의 카드의 요약 정보를 출력합니다.")
    @GetMapping("/cards")
    public ResponseEntity<List<CardSummary>> getCardSummary(
        @PathVariable Long boardId,
        @PathVariable Long columnId) {
        List<CardSummary> cardSummaries = cardService.getCardSummary(boardId, columnId);
        return new ResponseEntity<>(cardSummaries, HttpStatus.OK);
    }

    @Operation(summary = "카드 상세 정보 조회", description = "선택한 카드의 상세 정보를 출력합니다.")
    @GetMapping("/cards/{cardId}")
    public ResponseEntity<List<CardDetails>> getCardDetails(
        @PathVariable Long boardId,
        @PathVariable Long columnId,
        @PathVariable Long cardId
    ) {
        List<CardDetails> cardDetails = cardService.getCardDetails(boardId, columnId, cardId);
        return new ResponseEntity<>(cardDetails, HttpStatus.OK);
    }

    @Operation(summary = "카드 수정", description = "선택한 카드의 원하는 정보를 수정합니다.")
    @PatchMapping("/cards/{cardId}")
    public ResponseEntity<CommonResponse<CardResponse>> updateCard(
        @PathVariable Long boardId,
        @PathVariable Long columnId,
        @PathVariable Long cardId,
        @RequestBody CardUpdateRequest updateRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        cardService.updateCard(boardId, columnId, cardId, updateRequest, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).body(
            CommonResponse.<CardResponse>builder()
                .httpCode(HttpStatus.NO_CONTENT.value()).build());

    }

    @Operation(summary = "카드 삭제", description = "카드를 삭제합니다.")
    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<CommonResponse<String>> deleteCard(
        @PathVariable Long boardId,
        @PathVariable Long columnId,
        @PathVariable Long cardId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        cardService.deleteCard(boardId, columnId, cardId, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).body(
            CommonResponse.<String>builder()
                .httpCode(HttpStatus.NO_CONTENT.value()).build());
    }

    @Operation(summary = "카드 순서 변경", description = "선택한 카드의 순서를 변경합니다.")
    @PatchMapping("/cards/{cardId}/sequence")
    public ResponseEntity<CommonResponse<Void>> updatCardSequence(
        @PathVariable Long boardId,
        @PathVariable Long columnId,
        @PathVariable Long cardId,
        @RequestBody CardMoveRequest request
    ) {
        cardService.updatCardSequence(boardId, columnId, cardId, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).body(
            CommonResponse.<Void>builder()
                .httpCode(HttpStatus.NO_CONTENT.value()).build());
    }

    @Operation(summary = "카드 컬럼 이동", description = "선택한 카드의 컬럼을 변경합니다.")
    @PatchMapping("/cards/{cardId}/move")
    public ResponseEntity<CommonResponse<String>> moveCardToColumn(
        @PathVariable Long boardId,
        @PathVariable Long columnId,
        @PathVariable Long cardId,
        @RequestParam Long targetColumnId
    ) {
        cardService.moveCardToColumn(boardId, columnId, cardId, targetColumnId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).body(
            CommonResponse.<String>builder()
                .httpCode(HttpStatus.NO_CONTENT.value()).build());
    }
}
