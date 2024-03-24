package com.sparta.trello.domain.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.trello.domain.board.dto.BoardRequest;
import com.sparta.trello.domain.board.dto.BoardResponse;
import com.sparta.trello.domain.board.dto.GetBoardResponse;
import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.entity.BoardColorEnum;
import com.sparta.trello.domain.board.entity.BoardRoleEnum;
import com.sparta.trello.domain.board.entity.BoardUser;
import com.sparta.trello.domain.board.repository.BoardRepository;
import com.sparta.trello.domain.board.repository.BoardUserJpaRepository;
import com.sparta.trello.domain.board.service.BoardService;
import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.card.repository.CardRepository;
import com.sparta.trello.domain.column.entity.Columns;
import com.sparta.trello.domain.column.repository.ColumnRepository;
import com.sparta.trello.domain.user.entity.User;
import com.sparta.trello.domain.user.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BoardServiceTest {

    @Mock
    BoardRepository boardRepository;
    @Mock
    ColumnRepository columnRepository;
    @Mock
    BoardUserJpaRepository boardUserJpaRepository;
    @Mock
    CardRepository cardRepository;
    @Mock
    UserRepository userRepository;

    BoardService boardService;

    @BeforeEach
    void setUp() {
        boardService = new BoardService(boardRepository, boardUserJpaRepository, columnRepository,
            cardRepository, userRepository);
    }

    @Test
    @DisplayName("보드 생성 서비스 테스트")
    void createBoardTest() {
        // given
        String boardName = "testBoard";
        String description = "testDescription";
        Integer color = 1;
        BoardRequest boardRequest = BoardRequest.builder()
            .boardName(boardName)
            .description(description)
            .color(color)
            .build();
        User user = new User();
        Board board = new Board(boardRequest, BoardColorEnum.fromValue(color), user);
        BoardUser boardUser = new BoardUser(board, user, BoardRoleEnum.OWNER);

        when(boardRepository.save(any(Board.class))).thenReturn(board);
        when(boardUserJpaRepository.save(any(BoardUser.class))).thenReturn(boardUser);

        // when
        BoardResponse boardResponse = boardService.createBoard(boardRequest, user);

        // then
        assertEquals(boardName, boardResponse.getBoardName());
        assertEquals(description, boardResponse.getDescription());
        assertEquals(color, boardResponse.getColor().getValue());
    }

    @Test
    @DisplayName("보드 수정 테스트")
    void updateBoardTest() {
        // given
        Long boardId = 1L;
        Long userId = 1L;
        String newBoardName = "newBoardName";
        String newDescription = "newDescription";
        Integer newColor = 2;
        BoardRequest boardRequest = BoardRequest.builder()
            .boardName(newBoardName)
            .description(newDescription)
            .color(newColor)
            .build();
        User user = new User(userId, "test@naver.com", "testUser", "Password123!", "profile");
        Board board = new Board(boardId, newBoardName, newDescription, BoardColorEnum.fromValue(1),
            0,
            user);
        board.setBoardId(boardId);

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        // when
        BoardResponse updatedBoard = boardService.updateBoard(boardId, boardRequest, userId);

        // then
        assertEquals(newBoardName, updatedBoard.getBoardName());
        assertEquals(newDescription, updatedBoard.getDescription());
        assertEquals(newColor, updatedBoard.getColor().getValue());
    }

    @Test
    @DisplayName("보드 삭제 테스트")
    void deleteBoardTest() {
        // given
        Long boardId = 1L;
        Long userId = 1L;
        User user = new User(userId, "test@example.com", "testUser", "password", "profile1");
        Board board = new Board(boardId, "boardName", "description", BoardColorEnum.fromValue(1), 0,
            user);
        board.setBoardId(boardId);

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));

        // when
        boardService.deleteBoard(boardId, userId);

        // then
        verify(boardRepository, times(1)).deleteBoardAndRelateEntities(boardId);
    }

    @Test
    @DisplayName("보드 정보 조회 테스트")
    void getBoardListTest() {
        // given
        Long boardId = 1L;
        Long userId = 1L;
        User user = new User(userId, "test@example.com", "testUser", "password", "profile");
        Board board = new Board(boardId, "boardName", "description", BoardColorEnum.fromValue(1), 0,
            user);
        board.setBoardId(boardId);
        BoardUser boardUser = new BoardUser(board, user, BoardRoleEnum.MEMBER);
        List<Columns> columns = Arrays.asList(
            new Columns(1L, "columnName", 100L, 0, board),
            new Columns(2L, "columnName2", 200L, 0, board)
        );
        List<Card> cards = Arrays.asList(
            new Card("card", "des", "red", null, 100L, null, null),
            new Card("card2", "des2", "blue", null, 200L, null, null)
        );

        when(boardRepository.findById(boardId)).thenReturn(Optional.of(board));
        when(boardUserJpaRepository.findByBoardAndUser(board, user)).thenReturn(boardUser);
        when(columnRepository.findAllByBoardIdOrderBySequence(boardId)).thenReturn(columns);
        when(cardRepository.findAllByBoardId(boardId)).thenReturn(cards);

        // when
        GetBoardResponse boardResponse = boardService.getBoardList(boardId, user);

        // then
        assertEquals(boardId, boardResponse.getBoardId());
        assertEquals(board.getBoardName(), boardResponse.getBoardName());
        assertEquals(board.getDescription(), boardResponse.getDescription());
        assertEquals(board.getColor(), boardResponse.getColor());
        assertEquals(2, boardResponse.getColumns().size());
        assertEquals(2, boardResponse.getCards().size());
    }
}
