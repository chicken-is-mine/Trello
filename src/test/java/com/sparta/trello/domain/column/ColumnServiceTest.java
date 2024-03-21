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
import com.sparta.trello.domain.column.dto.ModifyColumnSequenceRequest;
import com.sparta.trello.domain.column.entity.Columns;
import com.sparta.trello.domain.column.repository.ColumnRepository;
import com.sparta.trello.domain.column.service.ColumnService;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

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
    @DisplayName("컬럼 수정 낙관적 락 동시성 테스트")
    void 컬럼_수정_낙관적_동시성_테스트() {
        // given
        AtomicInteger optimisticLockFailures = new AtomicInteger(0);

        Columns columns = Columns.builder().columnId(100L).columnName("컬럼").sequence(1000L).version(1).build();
        Columns prevColumn = Columns.builder().columnId(101L).columnName("Prev 컬럼").sequence(2000L)
            .build();
        Columns nextColumn = Columns.builder().columnId(102L).columnName("Next 컬럼").sequence(3000L)
            .build();

        given(columnRepository.findById(100L)).willReturn(Optional.of(columns));
//        given(columnRepository.findById(101L)).willReturn(Optional.of(prevColumn));
//        given(columnRepository.findById(102L)).willReturn(Optional.of(nextColumn));

        // when
        IntStream.range(0, 100).parallel().forEach(i -> {
            try {
                ModifyColumnSequenceRequest request = ModifyColumnSequenceRequest.builder()
                    .prevSequence(new Random().nextLong(5000L))
                    .nextSequence(new Random().nextLong(5001L, 10000L)).build();

                columnService.modifyColumnSequence(100L, 100L, request);
            } catch (ObjectOptimisticLockingFailureException e) {
                optimisticLockFailures.incrementAndGet();
            }
        });
// j미터, n그라인더, k6 : 과부하 테스트 툴
        // then
        System.out.println(columns.getSequence());
        System.out.println("낙관적 락 실패 횟수 : " + optimisticLockFailures.get());
    }
}
