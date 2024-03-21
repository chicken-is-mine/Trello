package com.sparta.trello.domain.board.service;

import com.sparta.trello.domain.board.dto.BoardRequest;
import com.sparta.trello.domain.board.dto.BoardResponse;
import com.sparta.trello.domain.board.dto.GetBoardResponse;
import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.entity.BoardColorEnum;
import com.sparta.trello.domain.board.entity.BoardRoleEnum;
import com.sparta.trello.domain.board.entity.BoardUser;
import com.sparta.trello.domain.board.repository.BoardRepository;
import com.sparta.trello.domain.board.repository.BoardUserJpaRepository;
import com.sparta.trello.domain.card.dto.CardResponse;
import com.sparta.trello.domain.card.repository.CardRepository;
import com.sparta.trello.domain.column.dto.GetColumnResponse;
import com.sparta.trello.domain.column.repository.ColumnRepository;
import com.sparta.trello.domain.user.entity.User;
import com.sparta.trello.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {

  private final BoardRepository boardRepository;
  private final UserRepository userRepository;
  private final BoardUserJpaRepository boardUserJpaRepository;
  private final ColumnRepository columnRepository;
  private final CardRepository cardRepository;

  public BoardResponse createBoard(BoardRequest boardRequest, User user) {
    BoardColorEnum color = BoardColorEnum.fromValue(boardRequest.getColor());
    Board board = new Board(boardRequest, color, user);
    Board savedBoard = boardRepository.save(board);

    BoardUser ownerBoardUser = new BoardUser(savedBoard, user, BoardRoleEnum.OWNER);
    boardUserJpaRepository.save(ownerBoardUser);

    return new BoardResponse(savedBoard);
  }


  public BoardResponse updateBoard(Long boardId, BoardRequest boardRequest, Long userId) {

    Board board = boardRepository.findById(boardId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 보드입니다."));

    if (!board.getUser().getId().equals(userId)) {
      throw new IllegalArgumentException("수정 권한이 없습니다.");
    }

    BoardColorEnum color = BoardColorEnum.fromValue(boardRequest.getColor());
    board.update(boardRequest.getBoardName(), boardRequest.getDescription(), color);

    return new BoardResponse(board);
  }


  public void deleteBoard(Long boardId, Long userId) {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

    Board board = boardRepository.findById(boardId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 보드 입니다."));

    if (!board.getUser().getId().equals(userId)) {
      throw new IllegalArgumentException("삭제 권한이 없습니다.");
    }

    // 관련된 BoardUser 엔티티 삭제
    List<BoardUser> boardUsers = boardUserJpaRepository.findByBoard(board);
    boardUserJpaRepository.deleteAll(boardUsers);

    // Board 엔티티 삭제
    boardRepository.delete(board);
  }

  public void inviteUser(Long boardId, List<Long> userIds, Long userId) {
    Board board = boardRepository.findById(boardId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시판입니다."));

    if (!board.getUser().getId().equals(userId)) {
      throw new IllegalArgumentException("초대 권한이 없습니다.");
    }

    List<User> existingMembers = boardRepository.findExistingMemberByBoard(board);

    List<User> users = boardRepository.findUsersByIds(userIds);

    List<User> newMembers = users.stream()
        .filter(user -> !existingMembers.contains(user))
        .toList();

    if (!newMembers.isEmpty()) {
      boardUserJpaRepository.saveAll(newMembers.stream()
          .map(user -> new BoardUser(board, user, BoardRoleEnum.MEMBER))
          .collect(Collectors.toList()));
    } else {
      throw new IllegalArgumentException("이미 초대된 사용자가 있습니다.");
    }
  }

  public GetBoardResponse getBoardList(Long boardId, User user) {
    Board board = boardRepository.findById(boardId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시판입니다."));

//    if (!board.getUser().equals(user)) {
//      throw new IllegalArgumentException("권한이 없습니다.");
//    }

    List<GetColumnResponse> columns = columnRepository.findAllByBoardIdOrderBySequence(boardId)
        .stream()
        .map(column -> new GetColumnResponse(column.getColumnId(), column.getColumnName(),
            column.getSequence()))
        .collect(Collectors.toList());

    List<CardResponse> cards = cardRepository.findAllByBoardId(boardId).stream()
        .map(card -> new CardResponse(card))
        .collect(Collectors.toList());

    return new GetBoardResponse(board.getBoardId(), board.getBoardName(), board.getDescription(),
        board.getColor(), columns, cards);
  }
}