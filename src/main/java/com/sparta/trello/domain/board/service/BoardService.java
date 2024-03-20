package com.sparta.trello.domain.board.service;

import com.sparta.trello.domain.board.dto.BoardRequest;
import com.sparta.trello.domain.board.dto.BoardResponse;
import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.entity.BoardColorEnum;
import com.sparta.trello.domain.board.entity.BoardRoleEnum;
import com.sparta.trello.domain.board.entity.BoardUser;
import com.sparta.trello.domain.board.repository.BoardRepository;
import com.sparta.trello.domain.board.repository.BoardUserJpaRepository;
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
    List<User> users = userRepository.findAllById(userIds);
    List<User> existingMembers = new ArrayList<>();
    List<User> newMembers = new ArrayList<>();
    for (User user : users) {
      if (boardUserJpaRepository.findByBoardAndUser(board, user).isPresent()) {
        existingMembers.add(user);
      } else {
        newMembers.add(user);
      }
    }
    if (!existingMembers.isEmpty()) {
      String existingUsernames = existingMembers.stream()
          .map(User::getUsername)
          .collect(Collectors.joining(", "));
      throw new IllegalArgumentException("이미 초대된 사용자가 있습니다: " + existingUsernames);
    }

    List<BoardUser> boardMembers = newMembers.stream()
        .map(user -> new BoardUser(board, user, BoardRoleEnum.MEMBER))
        .collect(Collectors.toList());
    boardUserJpaRepository.saveAll(boardMembers);
  }
}