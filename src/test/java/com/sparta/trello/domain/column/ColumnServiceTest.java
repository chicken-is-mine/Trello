package com.sparta.trello.domain.column;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;

import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.repository.BoardRepository;
import com.sparta.trello.domain.column.dto.ColumnResponse;
import com.sparta.trello.domain.column.dto.CreateColumnRequest;
import com.sparta.trello.domain.column.dto.ModifyColumnNameRequest;
import com.sparta.trello.domain.column.dto.ModifyColumnSequenceRequest;
import com.sparta.trello.domain.column.entity.Columns;
import com.sparta.trello.domain.column.repository.ColumnRepository;
import com.sparta.trello.domain.column.service.ColumnService;
import java.util.ArrayList;
import java.util.List;
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

    @Test
    void modifyColumnName_Success() {
        // given
        Columns columns = Columns.builder().columnId(100L).columnName("컬럼").sequence(1000L).build();
        given(columnRepository.findById(100L)).willReturn(Optional.of(columns));
        String name = "컬럼 수정";

        // when
        ColumnResponse response = columnService.modifyColumnName(100L,
            ModifyColumnNameRequest.builder().columnName(name).build());

        // then
        assertEquals(name, response.getColumnName());
    }

    @Test
    void modifyColumnSequence_Success() {
        // given
        Columns columns = Columns.builder().columnId(100L).columnName("컬럼").sequence(1000L).build();
        given(columnRepository.findById(100L)).willReturn(Optional.of(columns));
        Long prevSequence = 2000L, nextSequence = 3000L;
        Long between = (prevSequence + nextSequence) / 2;
        ModifyColumnSequenceRequest request = ModifyColumnSequenceRequest.builder()
            .prevSequence(prevSequence).nextSequence(nextSequence).build();

        // when
        ColumnResponse response = columnService.modifyColumnSequence(0L, 100L, request);

        // then
        assertEquals(between, response.getSequence());
    }

    @Test
    void modifyColumnSequence_EqualBetween() {
        // given
        Long sequence = 1000L;
        Columns columns = Columns.builder().columnId(100L).columnName("컬럼").sequence(sequence)
            .build();
        given(columnRepository.findById(100L)).willReturn(Optional.of(columns));
        Long prevSequence = 2000L, nextSequence = 2001L;
        Long between = (prevSequence + nextSequence) / 2;
        ModifyColumnSequenceRequest request = ModifyColumnSequenceRequest.builder()
            .prevSequence(prevSequence).nextSequence(nextSequence).build();

        Columns prevColumns = Columns.builder().columnId(101L).columnName("Prev 컬럼")
            .sequence(prevSequence).build();
        given(columnRepository.findBySequence(anyLong(), anyLong())).willReturn(prevColumns);

        // when
        ColumnResponse response = columnService.modifyColumnSequence(0L, 100L, request);

        // then
        assertEquals(between, response.getSequence());
        assertEquals(sequence, prevColumns.getSequence());
    }

    @Test
    void getColumnsOrderBySequence() {
        // given
        List<Columns> columns = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            columns.add(Columns.builder().columnId(100L + i).columnName("컬럼 " + (i + 1))
                .sequence(100L * i).build());
        }
        given(columnRepository.findAllByBoardIdOrderBySequence(anyLong())).willReturn(columns);

        // then
        List<ColumnResponse> responseList = columnService.getColumnsOrderBySequence(anyLong());

        // then
        for (ColumnResponse response : responseList) {
            System.out.printf("%s %s %s%n", response.getColumnId(), response.getColumnName(),
                response.getSequence());
        }
    }
}
