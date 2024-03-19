package com.sparta.trello.domain.column.controller;

import com.sparta.trello.domain.column.dto.CreateColumnRequest;
import com.sparta.trello.domain.column.dto.GetColumnResponse;
import com.sparta.trello.domain.column.dto.ModifyColumnNameRequest;
import com.sparta.trello.domain.column.dto.ModifyColumnSequenceRequest;
import com.sparta.trello.domain.column.service.ColumnService;
import com.sparta.trello.global.dto.CommonResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
                .httpCode(HttpStatus.NO_CONTENT.value()).build());
    }

    @DeleteMapping("/boards/{boardId}/columns/{columnId}")
    public ResponseEntity<CommonResponse<Void>> deleteColumn(
        @PathVariable Long boardId,
        @PathVariable Long columnId
    ) {
        columnService.deleteColumn(columnId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).body(
            CommonResponse.<Void>builder()
                .httpCode(HttpStatus.NO_CONTENT.value()).build());
    }

    @PatchMapping("/boards/{boardId}/columns/{columnId}/sequence")
    public ResponseEntity<CommonResponse<Void>> modifyColumnSequence(
        @PathVariable Long boardId,
        @PathVariable Long columnId,
        @RequestBody ModifyColumnSequenceRequest request
    ) {
        columnService.modifyColumnSequence(boardId, columnId, request);

        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).body(
            CommonResponse.<Void>builder()
                .httpCode(HttpStatus.NO_CONTENT.value()).build());
    }

    @GetMapping("/boards/{boardId}/columns")
    public ResponseEntity<CommonResponse<List<GetColumnResponse>>> getColumnsOrderBySequence(
        @PathVariable Long boardId
    ) {
        List<GetColumnResponse> responseList = columnService.getColumnsOrderBySequence(boardId);

        return ResponseEntity.status(HttpStatus.OK.value()).body(
            CommonResponse.<List<GetColumnResponse>>builder()
                .httpCode(HttpStatus.OK.value()).data(responseList).build());
    }
}