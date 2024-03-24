package com.sparta.trello.domain.column;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;

import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.repository.BoardRepository;
import com.sparta.trello.domain.column.dto.CreateColumnRequest;
import com.sparta.trello.domain.column.entity.Columns;
import com.sparta.trello.domain.column.repository.ColumnRepository;
import com.sparta.trello.domain.column.service.ColumnService;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ColumnServiceTest {

    @Mock
    ColumnRepository columnRepository;

    @Mock
    BoardRepository boardRepository;

    ColumnService columnService;

    @BeforeEach
    void setUp() {
        columnService = new ColumnService(columnRepository, boardRepository);
    }


    @Test
    void createColumn_Success() {
        // given
        Long boardId = 100L;
        Board board = Board.builder().boardId(boardId).build();
        given(boardRepository.findById(boardId)).willReturn(Optional.of(board));

        Columns columns = Columns.builder().columnId(100L).columnName("컬럼 1").sequence(1000L)
            .board(board).build();
        CreateColumnRequest request = new CreateColumnRequest(columns.getColumnName(),
            columns.getSequence());
        given(columnRepository.save(any(Columns.class))).willReturn(columns);

        // when
        columnService.createColumn(boardId, request);

        // then
        verify(boardRepository, times(1)).findById(boardId);
        verify(columnRepository, times(1)).save(any(Columns.class));
    }

    @Test
    void createColumn_ThrowsNoSuchElementException() {
        // given
        Long boardId = 100L;
        CreateColumnRequest request = new CreateColumnRequest("Column 1", 1000L);

        given(boardRepository.findById(boardId)).willReturn(Optional.empty());

        // when & then
        assertThrows(NoSuchElementException.class,
            () -> columnService.createColumn(boardId, request));
        verify(boardRepository, times(1)).findById(boardId);
        verify(columnRepository, never()).save(any(Columns.class));
    }
}
