package com.sparta.trello.domain.column.controller;

import com.sparta.trello.domain.column.dto.ColumnResponse;
import com.sparta.trello.domain.column.dto.CreateColumnRequest;
import com.sparta.trello.domain.column.dto.ModifyColumnNameRequest;
import com.sparta.trello.domain.column.dto.ModifyColumnSequenceRequest;
import com.sparta.trello.domain.column.service.ColumnService;
import com.sparta.trello.global.dto.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Column API", description = "컬럼 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ColumnController {

    private final ColumnService columnService;

    @Operation(summary = "컬럼 생성", description = "입력된 boardId에 컬럼을 생성합니다.")
    @PostMapping("/boards/{boardId}/columns")
    public ResponseEntity<CommonResponse<ColumnResponse>> createColumn(
        @PathVariable Long boardId,
        @RequestBody CreateColumnRequest request
    ) {
        ColumnResponse response = columnService.createColumn(boardId, request);

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(
            CommonResponse.<ColumnResponse>builder()
                .httpCode(HttpStatus.CREATED.value()).data(response).build());
    }

    @Operation(summary = "컬럼 이름 수정", description = "입력된 columnId의 이름을 수정합니다.")
    @PatchMapping("/boards/{boardId}/columns/{columnId}")
    public ResponseEntity<CommonResponse<ColumnResponse>> modifyColumnName(
        @PathVariable Long boardId,
        @PathVariable Long columnId,
        @RequestBody ModifyColumnNameRequest request
    ) {
        ColumnResponse response = columnService.modifyColumnName(columnId, request);

        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).body(
            CommonResponse.<ColumnResponse>builder()
                .httpCode(HttpStatus.NO_CONTENT.value()).data(response).build());
    }

    @Operation(summary = "컬럼 삭제", description = "입력된 columnId를 삭제합니다.")
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

    @Operation(summary = "컬럼 순서 수정", description = "입력된 columnId의 순서를 수정합니다.")
    @PatchMapping("/boards/{boardId}/columns/{columnId}/sequence")
    public ResponseEntity<CommonResponse<ColumnResponse>> modifyColumnSequence(
        @PathVariable Long boardId,
        @PathVariable Long columnId,
        @RequestBody ModifyColumnSequenceRequest request
    ) {
        ColumnResponse response = columnService.modifyColumnSequence(boardId, columnId, request);

        return ResponseEntity.status(HttpStatus.NO_CONTENT.value()).body(
            CommonResponse.<ColumnResponse>builder()
                .httpCode(HttpStatus.NO_CONTENT.value()).data(response).build());
    }

    @Operation(summary = "컬럼 조회", description = "컬럼을 순서 정렬해 조회합니다.")
    @GetMapping("/boards/{boardId}/columns")
    public ResponseEntity<CommonResponse<List<ColumnResponse>>> getColumnsOrderBySequence(
        @PathVariable Long boardId
    ) {
        List<ColumnResponse> responseList = columnService.getColumnsOrderBySequence(boardId);

        return ResponseEntity.status(HttpStatus.OK.value()).body(
            CommonResponse.<List<ColumnResponse>>builder()
                .httpCode(HttpStatus.OK.value()).data(responseList).build());
    }
}