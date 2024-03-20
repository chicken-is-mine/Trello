package com.sparta.trello.domain.board.repository;

import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.entity.BoardUser;
import com.sparta.trello.domain.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardUserJpaRepository extends JpaRepository <BoardUser,Long> {

  Optional<BoardUser> findByBoardAndUser(Board board, User user);

  List<BoardUser> findByBoard(Board board);
}
