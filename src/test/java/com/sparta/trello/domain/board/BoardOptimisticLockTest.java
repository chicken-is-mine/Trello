package com.sparta.trello.domain.board;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import com.sparta.trello.domain.board.dto.BoardRequest;
import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.entity.BoardColorEnum;
import com.sparta.trello.domain.board.entity.BoardRoleEnum;
import com.sparta.trello.domain.board.entity.BoardUser;
import com.sparta.trello.domain.board.repository.BoardRepository;
import com.sparta.trello.domain.board.repository.BoardUserJpaRepository;
import com.sparta.trello.domain.board.service.BoardService;
import com.sparta.trello.domain.column.entity.Columns;
import com.sparta.trello.domain.column.repository.ColumnRepository;
import com.sparta.trello.domain.user.entity.User;
import com.sparta.trello.domain.user.repository.UserRepository;
import com.sparta.trello.global.aop.BoardUserValidateAspect;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2, replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.properties")
public class BoardOptimisticLockTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    ColumnRepository columnRepository;
    @Autowired
    BoardUserJpaRepository boardUserJpaRepository;

    @Autowired
    BoardService boardService;

    @MockBean
    private BoardUserValidateAspect boardUserValidateAspect;

    private User user;
    private Board board;
    private Columns columns;

    @BeforeEach
    void setUp() {
        user = User.builder().email("abc123@naver.com").username("abc123").password("abc!2345")
            .profile("").build();
        user = userRepository.save(user);

        board = Board.builder().boardName("보드").color(BoardColorEnum.BLACK).description("")
            .user(user).build();
        board = boardRepository.save(board);

        BoardUser boardUser = new BoardUser(board, user, BoardRoleEnum.OWNER);
        boardUserJpaRepository.save(boardUser);

        columns = Columns.builder().columnName("컬럼").sequence(1000L).board(board).build();
        columns = columnRepository.save(columns);

        doNothing().when(boardUserValidateAspect).validateBoardUser(any(JoinPoint.class));
    }

    @Test
    @DisplayName("보드 수정 낙관적 락 동시성 테스트")
    void testOptimisticLocking() {
        // given
        AtomicInteger optimisticLockFailures = new AtomicInteger(0);

        // when
        IntStream.range(0, 1000).parallel().forEach(i -> {
            try {
                BoardRequest boardRequest = BoardRequest.builder()
                    .boardName("new board name ")
                    .description("new description")
                    .color(BoardColorEnum.RED.getValue())
                    .build();

                boardService.updateBoard(1L, boardRequest, 1L);
            } catch (ObjectOptimisticLockingFailureException e) {
                optimisticLockFailures.incrementAndGet();
            }
        });

        // then
        System.out.println("낙관적 락 실패 횟수 : " + optimisticLockFailures.get());
    }
}
