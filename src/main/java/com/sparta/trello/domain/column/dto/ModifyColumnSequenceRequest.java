package com.sparta.trello.domain.column.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModifyColumnSequenceRequest {

    private Long prevSequence;
    private Long nextSequence;
}
