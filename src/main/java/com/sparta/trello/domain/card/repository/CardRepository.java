package com.sparta.trello.domain.card.repository;

import com.sparta.trello.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CardRepository extends JpaRepository<Card, Long> ,
    QuerydslPredicateExecutor<Card> {
}
