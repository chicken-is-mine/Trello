package com.sparta.trello.domain.card.repository;

import com.sparta.trello.domain.card.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkerRepository extends JpaRepository<Worker,Long> {
}
