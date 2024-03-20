package com.sparta.trello.domain.card.service;

import com.sparta.trello.domain.board.entity.BoardUser;
import com.sparta.trello.domain.board.repository.BoardUserJpaRepository;
import com.sparta.trello.domain.card.dto.CardDetails;
import com.sparta.trello.domain.card.dto.CardRequest;
import com.sparta.trello.domain.card.dto.CardSummary;
import com.sparta.trello.domain.card.dto.CardUpdateRequest;
import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.card.entity.Worker;
import com.sparta.trello.domain.card.repository.CardRepository;
import com.sparta.trello.domain.column.entity.Columns;
import com.sparta.trello.domain.column.repository.ColumnRepository;
import com.sparta.trello.domain.user.entity.User;
import com.sparta.trello.domain.user.repository.UserRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final ColumnRepository columnRepository;
    private final UserRepository userRepository;
    private final BoardUserJpaRepository boardUserJpaRepository;

    //카드 생성
    @Transactional
    public Card createCard(Long columnId, CardRequest request, User user) {
        Columns columns = findColumn(columnId);
        Optional<BoardUser> boardUserOptional = boardUserJpaRepository.findById(user.getId());
        return cardRepository.save(new Card(request, columns, user));
    }

    //카드 요약
    public List<CardSummary> getCardSummary(Long columnId) {
        return cardRepository.findCardsSummaryByColumnId(columnId);
    }

    //카드 상세 정보
    public List<CardDetails> getCardDetails(Long columnId, Long cardId) {
        return cardRepository.findCardDetailsByColumnId(columnId, cardId);
    }

    //카드 업데이트
    @Transactional
    public Card updateCard(Long columnId, Long cardId, CardUpdateRequest updateRequest, User user) {
        findColumn(columnId);
        Card card = findCard(cardId);
        Optional<BoardUser> boardUserOptional = boardUserJpaRepository.findById(user.getId());
        if(!boardUserOptional.isPresent()) {
            throw new NoSuchElementException("워크스페이스 권한이 없는 사용자입니다.");
        }

        boolean updated = false;
        if (updateRequest.getCardName() != null && !updateRequest.getCardName().equals(card.getCardName())) {
            card.updateCardName(updateRequest.getCardName());
            updated = true;
        }
        if (updateRequest.getDescription() != null && !updateRequest.getDescription().equals(card.getDescription())) {
            card.updateDescription(updateRequest.getDescription());
            updated = true;
        }
        if (updateRequest.getColor() != null && !updateRequest.getColor().equals(card.getColor())) {
            card.updateColor(updateRequest.getColor());
            updated = true;
        }
        if (updateRequest.getDueDate() != null && !updateRequest.getDueDate().equals(card.getDueDate())) {
            card.updateDueDate(updateRequest.getDueDate());
            updated = true;
        }

        // 작업자 추가
        if (updateRequest.getWorkerId() != null) {
            User worker = userRepository.findById(updateRequest.getWorkerId())
                .orElseThrow(() -> new NoSuchElementException("해당 ID에 해당하는 작업자를 찾을 수 없습니다"));
            Worker newWorker = new Worker(worker);
            card.addWorker(newWorker.getUser());
            updated = true;
        }

        // 작업자 제거
        if (updateRequest.getRemoveWorkerId() != null) {
            Worker removeWorker = card.getWorkers().stream()
                .filter(worker -> worker.getUser().getId().equals(updateRequest.getRemoveWorkerId()))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 ID에 해당하는 작업자를 찾을 수 없습니다"));
            card.removeWorker(removeWorker);
            updated = true;
        }

        // 변경된 내용이 있을 경우에만 저장
        if (updated) {
            cardRepository.save(card);
        }

        return card;
    }

    //카드 삭제
    @Transactional
    public void deleteCard(Long columnId, Long cardId, User user) {
        findColumn(columnId);
        Optional<BoardUser> boardUserOptional = boardUserJpaRepository.findById(user.getId());
        if(!boardUserOptional.isPresent()) {
            throw new NoSuchElementException("워크스페이스 권한이 없는 사용자입니다.");
        }
        Card card = findCard(cardId);
        cardRepository.delete(card);
    }


    private Columns findColumn(Long columnId) {
        return columnRepository.findById(columnId).orElseThrow(() -> new IllegalArgumentException("Column not found"));
    }

    private Card findCard(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(() -> new IllegalArgumentException("Card not found"));
    }
}
