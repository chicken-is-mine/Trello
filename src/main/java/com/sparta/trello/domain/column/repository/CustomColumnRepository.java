package com.sparta.trello.domain.column.repository;

import com.sparta.trello.domain.column.entity.Columns;
import java.util.List;

public interface CustomColumnRepository {

    Columns findBySequence(Long boardId, Long sequence);

    List<Columns> findAllByBoardIdOrderBySequence(Long boardId);
}
