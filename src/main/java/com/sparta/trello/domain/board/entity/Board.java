package com.sparta.trello.domain.board.entity;

import com.sparta.trello.domain.board.dto.BoardRequest;
import com.sparta.trello.domain.user.entity.User;
import com.sparta.trello.global.entity.Timestamped;
import jakarta.persistence.*;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TB_BOARD")
public class Board extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Version
    private int version;

    @Column(nullable = false)
    private String boardName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private BoardColorEnum color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Board(BoardRequest boardRequest, BoardColorEnum color, User user) {
        this.boardName = boardRequest.getBoardName();
        this.description = boardRequest.getDescription();
        this.color = color;
        this.user = user;
    }

    public void update(String boardName, String description, BoardColorEnum color) {
        this.boardName = boardName;
        this.description = description;
        this.color = color;
    }


}