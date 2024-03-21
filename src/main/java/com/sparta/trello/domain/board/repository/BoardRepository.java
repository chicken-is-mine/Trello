package com.sparta.trello.domain.board.repository;

import com.sparta.trello.domain.board.entity.Board;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>,CustomBoardRepository {

}
