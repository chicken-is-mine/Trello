package com.sparta.trello.domain.card.controller;

import com.sparta.trello.domain.card.dto.CardRequest;
import com.sparta.trello.domain.card.dto.CardResponse;
import com.sparta.trello.domain.card.dto.CardUpdateRequest;
import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.card.service.CardService;
import com.sparta.trello.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards/{boardId}/columns/{columnId}")
public class CardController {
    private final CardService cardService;

    @PostMapping("/cards")
    public ResponseEntity<CardResponse> createCard(
        @PathVariable Long columnId,
        @RequestBody CardRequest request,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Card card = cardService.createCard(columnId, request, userDetails.getUser());
        return new ResponseEntity<>(new CardResponse(card), HttpStatus.CREATED);
    }

    @PatchMapping("/cards/{cardId}")
    public ResponseEntity<CardResponse> updateCard(
        @PathVariable Long columnId,
        @PathVariable Long cardId,
        @RequestBody CardUpdateRequest updateRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Card card = cardService.updateCard(columnId, cardId, updateRequest, userDetails.getUser());
        return new ResponseEntity<>(new CardResponse(card), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<String> deleteCard(
        @PathVariable Long columnId,
        @PathVariable Long cardId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        cardService.deleteCard(columnId, cardId, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
