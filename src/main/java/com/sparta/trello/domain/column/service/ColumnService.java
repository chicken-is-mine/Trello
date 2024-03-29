package com.sparta.trello.domain.column.service;

import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.repository.BoardRepository;
import com.sparta.trello.domain.column.dto.ColumnResponse;
import com.sparta.trello.domain.column.dto.CreateColumnRequest;
import com.sparta.trello.domain.column.dto.ModifyColumnNameRequest;
import com.sparta.trello.domain.column.dto.ModifyColumnSequenceRequest;
import com.sparta.trello.domain.column.entity.Columns;
import com.sparta.trello.domain.column.repository.ColumnRepository;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ColumnService {

    private final ColumnRepository columnRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public ColumnResponse createColumn(Long boardId, CreateColumnRequest request) {
        Board board = validateExistBoard(boardId);

        Columns createdColumn = columnRepository.save(
            Columns.builder().columnName(request.getColumnName()).sequence(
                request.getSequence()).board(board).build());

        return ColumnResponse.builder()
            .columnId(createdColumn.getColumnId())
            .columnName(createdColumn.getColumnName())
            .sequence(createdColumn.getSequence())
            .build();
    }

    @Transactional
    public ColumnResponse modifyColumnName(Long columnId, ModifyColumnNameRequest request) {
        Columns columns = validateExistColumn(columnId);

        columns.setColumnName(request.getColumnName());

        return ColumnResponse.builder()
            .columnId(columnId)
            .columnName(columns.getColumnName())
            .sequence(columns.getSequence())
            .build();
    }

    public void deleteColumn(Long columnId) {
        columnRepository.deleteById(columnId);
    }

    @Transactional
    public ColumnResponse modifyColumnSequence(Long boardId, Long columnId,
        ModifyColumnSequenceRequest request) {
        Columns columns = validateExistColumn(columnId);

        Long between = (request.getPrevSequence() + request.getNextSequence()) / 2;

        if (between.equals(request.getPrevSequence())) {
            Columns prevColumns = columnRepository.findBySequence(boardId,
                request.getPrevSequence());

            prevColumns.setSequence(columns.getSequence());
        } else if (between.equals(request.getNextSequence())) {
            Columns nextColumns = columnRepository.findBySequence(boardId,
                request.getNextSequence());

            nextColumns.setSequence(columns.getSequence());
        }

        columns.setSequence(between);

        return ColumnResponse.builder()
            .columnId(columnId)
            .columnName(columns.getColumnName())
            .sequence(columns.getSequence())
            .build();
    }

    public List<ColumnResponse> getColumnsOrderBySequence(Long boardId) {
        return columnRepository.findAllByBoardIdOrderBySequence(boardId).stream()
            .map(ColumnResponse::new).toList();
    }

    private Columns validateExistColumn(Long columnId) {
        return columnRepository.findById(columnId).orElseThrow(
            () -> new NoSuchElementException("해당 컬럼을 찾을 수 없습니다.")
        );
    }

    private Board validateExistBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(
            () -> new NoSuchElementException("해당 보드를 찾을 수 없습니다.")
        );
    }
}
