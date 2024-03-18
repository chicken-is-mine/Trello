package com.sparta.trello.domain.column.controller;

import com.sparta.trello.domain.column.dto.ColumnCreateRequest;
import com.sparta.trello.domain.column.service.ColumnService;
import com.sparta.trello.global.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ColumnController {

    private final ColumnService columnService;

    @PostMapping("/boards/{boardId}/columns")
    public ResponseEntity<CommonResponse<Void>> createColumn(
        @PathVariable Long boardId,
        @RequestBody ColumnCreateRequest request
    ) {
        columnService.createColumn(boardId, request);

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(
            CommonResponse.<Void>builder()
                .httpCode(HttpStatus.CREATED.value()).build());
    }
}