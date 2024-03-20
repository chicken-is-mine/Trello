package com.sparta.trello.domain.board.entity;

public enum BoardColorEnum {
  RED(1), YELLOW(2), BLUE(3), WHITE(4), BLACK(5);

  private final int value;

  BoardColorEnum(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public static BoardColorEnum fromValue(int value) {
    for (BoardColorEnum color : BoardColorEnum.values()) {
      if (color.getValue() == value) {
        return color;
      }
    }
    throw new IllegalArgumentException("Invalid color value: " + value);
  }
}