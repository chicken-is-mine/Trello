package com.sparta.trello.domain.card.entity;

import com.sparta.trello.domain.card.dto.CardRequest;
import com.sparta.trello.domain.column.entity.Columns;
import com.sparta.trello.domain.user.entity.User;
import com.sparta.trello.global.entity.Timestamped;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "TB_CARD")
public class Card extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    @Column(nullable = false)
    private String cardName;

    @Lob
    private String description;

    @Column
    private String color;

    @Column
    private LocalDateTime dueDate;

    @Column(nullable = false, unique = true)
    private Long sequence;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "column_id", nullable = false)
    private Columns column;

    @OneToMany(mappedBy = "card", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Worker> workers;


    public Card(String cardName, String description, String color, LocalDateTime dueDate,
        Long sequence, User user, Columns columns) {
        this.cardName = cardName;
        this.description = description;
        this.color = color;
        this.dueDate = dueDate;
        this.sequence = sequence;
        this.user = user;
        this.column = columns;
    }

    public Card(CardRequest request, Columns columns, User user) {
        this.cardName = request.getCardName();
        this.sequence = request.getSequence();
        this.column = columns;
        this.user = user;
    }


    public void updateCardName(String cardName) {
        this.cardName = cardName;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateColor(String color) {
        this.color = color;
    }

    public void updateDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public void addWorker(User user) {
        Worker worker = new Worker(user, this);
        workers.add(worker);
    }

    public void removeWorker(Worker worker) {
        workers.remove(worker);
    }
}
