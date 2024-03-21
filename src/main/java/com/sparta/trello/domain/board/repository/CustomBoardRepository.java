package com.sparta.trello.domain.board.repository;

import com.sparta.trello.domain.board.dto.BoardInfo;
import com.sparta.trello.domain.board.entity.Board;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomBoardRepository {

    List<BoardInfo> findByUser_Id(Long userId);

}
