package com.sparta.trello.domain.board.repository;

import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.entity.BoardUser;
import com.sparta.trello.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardUserJpaRepository extends JpaRepository<BoardUser, Long> {

    BoardUser findByBoardAndUser(Board board, User user);

    List<BoardUser> findByBoard(Board board);

    @Query("select bu from BoardUser bu where bu.board.boardId = ?1 and bu.user.id = ?2")
    BoardUser findByBoardIdAndUserId(Long boardId, Long userId);
}
