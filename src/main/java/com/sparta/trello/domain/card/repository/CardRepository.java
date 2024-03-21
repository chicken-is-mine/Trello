package com.sparta.trello.domain.card.repository;

import com.sparta.trello.domain.card.dto.CardInfo;
import com.sparta.trello.domain.card.entity.Card;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> ,
    CardRepositoryCustom {

}
