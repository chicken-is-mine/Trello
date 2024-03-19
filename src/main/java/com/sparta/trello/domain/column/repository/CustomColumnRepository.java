package com.sparta.trello.domain.column.repository;

import com.sparta.trello.domain.column.entity.Columns;

public interface CustomColumnRepository {

    Columns findBySequence(Long boardId, Long sequence);
}
