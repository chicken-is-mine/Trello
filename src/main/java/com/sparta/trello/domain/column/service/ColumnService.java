package com.sparta.trello.domain.column.service;

import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.repository.BoardRepository;
import com.sparta.trello.domain.column.dto.ColumnCreateRequest;
import com.sparta.trello.domain.column.entity.Columns;
import com.sparta.trello.domain.column.repository.ColumnRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ColumnService {

    private final ColumnRepository columnRepository;
    private final BoardRepository boardRepository;

    public void createColumn(Long boardId, ColumnCreateRequest request) {
        Board board = validateBoard(boardId);

        columnRepository.save(
            Columns.builder().columnName(request.getColumnName()).sequence(
                request.getSequence()).board(board).build());
    }

    private Board validateBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
            () -> new NoSuchElementException("해당 보드를 찾을 수 없습니다.")
        );
    }
}
