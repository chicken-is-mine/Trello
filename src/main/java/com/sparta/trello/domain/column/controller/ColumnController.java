package com.sparta.trello.domain.column.controller;

import com.sparta.trello.domain.column.dto.CreateColumnRequest;
import com.sparta.trello.domain.column.dto.ModifyColumnNameRequest;
import com.sparta.trello.domain.column.service.ColumnService;
import com.sparta.trello.global.dto.CommonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
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
        @RequestBody CreateColumnRequest request
    ) {
        columnService.createColumn(boardId, request);

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(
            CommonResponse.<Void>builder()
                .httpCode(HttpStatus.CREATED.value()).build());
    }

    @PatchMapping("/boards/{boardId}/columns/{columnId}")
    public ResponseEntity<CommonResponse<Void>> modifyColumnName(
        @PathVariable Long boardId,
        @PathVariable Long columnId,
        @RequestBody ModifyColumnNameRequest request
    ) {
        columnService.modifyColumnName(columnId, request);

        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).body(
            CommonResponse.<Void>builder()
                .httpCode(HttpStatus.CREATED.value()).build());
    }
}