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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards/{boardId}/columns/{columnId}")
public class CardController {

    private final CardService cardService;

    @PostMapping("/cards")
    public ResponseEntity<CommonResponse<CardResponse>> createCard(
        @PathVariable Long columnId,
        @RequestBody CardRequest request,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        cardService.createCard(columnId, request, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).body(
            CommonResponse.<CardResponse>builder()
                .httpCode(HttpStatus.NO_CONTENT.value()).build());
    }

    @GetMapping("/cards")
    public ResponseEntity<List<CardSummary>> getCardSummary(@PathVariable Long columnId) {
        List<CardSummary> cardSummaries = cardService.getCardSummary(columnId);
        return new ResponseEntity<>(cardSummaries, HttpStatus.OK);
    }

    @GetMapping("/cards/{cardId}")
    public ResponseEntity<List<CardDetails>> getCardDetails(
        @PathVariable Long columnId,
        @PathVariable Long cardId
    ) {
        List<CardDetails> cardDetails = cardService.getCardDetails(columnId, cardId);
        return new ResponseEntity<>(cardDetails, HttpStatus.OK);
    }

    @PatchMapping("/cards/{cardId}")
    public ResponseEntity<CommonResponse<CardResponse>> updateCard(
        @PathVariable Long columnId,
        @PathVariable Long cardId,
        @RequestBody CardUpdateRequest updateRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        cardService.updateCard(columnId, cardId, updateRequest, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).body(
            CommonResponse.<CardResponse>builder()
                .httpCode(HttpStatus.NO_CONTENT.value()).build());

    }

    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<CommonResponse<String>> deleteCard(
        @PathVariable Long columnId,
        @PathVariable Long cardId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        cardService.deleteCard(columnId, cardId, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).body(
            CommonResponse.<String>builder()
                .httpCode(HttpStatus.NO_CONTENT.value()).build());
    }

    @PatchMapping("/cards/{cardId}/sequence")
    public ResponseEntity<CommonResponse<Void>> updatCardSequence(
        @PathVariable Long columnId,
        @PathVariable Long cardId,
        @RequestBody CardMoveRequest request
    ) {
        cardService.updatCardSequence(columnId, cardId, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).body(
            CommonResponse.<Void>builder()
                .httpCode(HttpStatus.NO_CONTENT.value()).build());
    }

    @PatchMapping("/cards/{cardId}/move")
    public ResponseEntity<CommonResponse<String>> moveCardToColumn(
        @PathVariable Long columnId,
        @PathVariable Long cardId,
        @RequestParam Long targetColumnId
    ) {
        cardService.moveCardToColumn(columnId, cardId, targetColumnId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).body(
            CommonResponse.<String>builder()
                .httpCode(HttpStatus.NO_CONTENT.value()).build());
    }
}
