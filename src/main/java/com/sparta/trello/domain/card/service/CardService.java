package com.sparta.trello.domain.card.service;

import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.repository.BoardRepository;
import com.sparta.trello.domain.card.dto.CardRequest;
import com.sparta.trello.domain.card.dto.CardUpdateRequest;
import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.card.entity.Worker;
import com.sparta.trello.domain.card.repository.CardRepository;
import com.sparta.trello.domain.card.repository.WorkerRepository;
import com.sparta.trello.domain.column.entity.Columns;
import com.sparta.trello.domain.column.repository.ColumnRepository;
import com.sparta.trello.domain.user.entity.User;
import com.sparta.trello.domain.user.repository.UserRepository;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final BoardRepository boardRepository;
    private final ColumnRepository columnRepository;
    private final WorkerRepository workerRepository;
    private final UserRepository userRepository;

    public Card createCard(Long columnId, CardRequest request, User user) {
        Columns columns = findColumn(columnId);
        return cardRepository.save(new Card(request, columns, user));
    }

    @Transactional
    public Card updateCard(Long columnId, Long cardId, CardUpdateRequest updateRequest, User user) {
        findColumn(columnId);
        Card card = findCard(cardId);

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
            card.addWorker(worker);
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

        // 변경된 내용이 있을 경우에만 저장합니다.
        if (updated) {
            cardRepository.save(card);
        }

        return card;
    }

    public void deleteCard(Long columnId, Long cardId, User user) {
        findColumn(columnId);
        Card card = findCard(cardId);
        cardRepository.delete(card);
    }

    private User findUser(User user) {
        return userRepository.findById(user.getId()).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    private Board findBoard(Long boardId) {
        return boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("Board not found"));
    }

    private Columns findColumn(Long columnId) {
        return columnRepository.findById(columnId).orElseThrow(() -> new IllegalArgumentException("Column not found"));
    }

    private Worker findWorker(Long workerId) {
        return workerRepository.findById(workerId).orElseThrow(() -> new IllegalArgumentException("Worker not found"));
    }

    private Card findCard(Long cardId) {
        return cardRepository.findById(cardId).orElseThrow(() -> new IllegalArgumentException("Card not found"));
    }


}
