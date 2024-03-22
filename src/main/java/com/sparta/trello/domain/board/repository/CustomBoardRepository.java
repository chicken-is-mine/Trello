package com.sparta.trello.domain.board.repository;


import com.sparta.trello.domain.board.dto.BoardInfo;
import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.user.entity.User;

import java.util.List;

import org.springframework.stereotype.Repository;

@Repository
public interface CustomBoardRepository {

    List<User> findExistingMemberByBoard(Board board);

    List<User> findUsersByIds(List<Long> userIds);

    List<BoardInfo> findByUser_Id(Long userId);

    void deleteBoardAndRelateEntities(Long boardId);
}
