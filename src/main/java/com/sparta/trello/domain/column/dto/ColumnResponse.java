package com.sparta.trello.domain.column.dto;

import com.sparta.trello.domain.column.entity.Columns;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColumnResponse {

    private Long columnId;

    private String columnName;

    private Long sequence;

    public ColumnResponse(Columns columns) {
        this.columnId = columns.getColumnId();
        this.columnName = columns.getColumnName();
        this.sequence = columns.getSequence();
    }
}
