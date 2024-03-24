package com.sparta.trello.domain.board;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.entity.BoardColorEnum;
import com.sparta.trello.domain.board.entity.BoardRoleEnum;
import com.sparta.trello.domain.board.entity.BoardUser;
import com.sparta.trello.domain.board.repository.BoardRepository;
import com.sparta.trello.domain.board.repository.BoardUserJpaRepository;
import com.sparta.trello.domain.user.entity.User;
import com.sparta.trello.domain.user.repository.UserRepository;
import com.sparta.trello.global.config.QueryDslConfig;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase
@ActiveProfiles("test")
@Import(QueryDslConfig.class)
public class BoardRepositoryTest {

  @Autowired
  BoardRepository boardRepository;

  @Autowired
  BoardUserJpaRepository boardUserJpaRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  JPAQueryFactory jpaQueryFactory;

  private User user1, user2;
  private Board board1, board2;

  @BeforeEach
  public void setUp() {
    user1 = User.builder().email("test1@naver.com").password("password123!").username("test1")
        .profile("profile1").build();
    userRepository.save(user1);

    user2 = User.builder().email("test2@naver.com").password("password123!").username("test2")
        .profile("profile2").build();
    userRepository.save(user2);

    board1 = Board.builder().boardName("board1").color(BoardColorEnum.fromValue(2))
        .description("board1")
        .user(user1)
        .build();
    boardRepository.save(board1);

    board2 = Board.builder().boardName("board2").color(BoardColorEnum.fromValue(3))
        .description("board2")
        .user(user2)
        .build();
    boardRepository.save(board2);

    BoardUser boardUser1 = new BoardUser(board1, user1, BoardRoleEnum.OWNER);
    boardUserJpaRepository.save(boardUser1);

    BoardUser boardUser2 = new BoardUser(board1, user2, BoardRoleEnum.MEMBER);
    boardUserJpaRepository.save(boardUser2);

    BoardUser boardUser3 = new BoardUser(board2, user2, BoardRoleEnum.OWNER);
    boardUserJpaRepository.save(boardUser3);
  }

  @Test
  @DisplayName("보드에 속한 멤버 조회 테스트")
  public void findExistingMemberByBoard() {
    // given
    Board board = board1;

    // when
    List<User> existingMembers = boardRepository.findExistingMemberByBoard(board);

    // then
    assertEquals(2, existingMembers.size());
  }

}
