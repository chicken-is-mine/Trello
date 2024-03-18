package com.sparta.trello.domain.column.repository;

import com.sparta.trello.domain.column.entity.Columns;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColumnRepository extends JpaRepository<Columns, Long> {

}
