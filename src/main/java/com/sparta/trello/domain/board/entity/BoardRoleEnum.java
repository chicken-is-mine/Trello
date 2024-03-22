package com.sparta.trello.domain.board.entity;

public enum BoardRoleEnum {
    OWNER(1),
    MEMBER(2);

    private final int value;

    BoardRoleEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
