package com.sparta.trello.domain.board.repository;

import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.entity.BoardUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardUserJpaRepository extends JpaRepository<BoardUser, Long> {
  List<BoardUser> findByBoard(Board board);
}
