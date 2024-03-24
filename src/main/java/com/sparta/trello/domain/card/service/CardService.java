package com.sparta.trello.domain.card.service;

import com.sparta.trello.domain.card.dto.CardDetails;
import com.sparta.trello.domain.card.dto.CardMoveRequest;
import com.sparta.trello.domain.card.dto.CardRequest;
import com.sparta.trello.domain.card.dto.CardResponse;
import com.sparta.trello.domain.card.dto.CardSummary;
import com.sparta.trello.domain.card.dto.CardUpdateRequest;
import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.card.entity.Worker;
import com.sparta.trello.domain.card.repository.CardRepository;
import com.sparta.trello.domain.card.repository.WorkerRepository;
import com.sparta.trello.domain.column.entity.Columns;
import com.sparta.trello.domain.column.repository.ColumnRepository;
import com.sparta.trello.domain.comment.dto.CommentResponse;
import com.sparta.trello.domain.comment.service.CommentService;
import com.sparta.trello.domain.user.entity.User;
import com.sparta.trello.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final ColumnRepository columnRepository;
    private final UserRepository userRepository;
    private final WorkerRepository workerRepository;
    private final CommentService commentService;

    //카드 생성
    @Transactional
    public Card createCard(Long columnId, CardRequest request, User user) {
        Columns columns = findColumn(columnId);

        return cardRepository.save(new Card(request, columns, user));
    }

    //카드 요약
    @Transactional(readOnly = true)
    public List<CardSummary> getCardSummary(Long columnId) {
        return cardRepository.findCardsSummaryByColumnId(columnId);
    }

    //카드 상세 정보
    @Transactional(readOnly = true)
    public List<CardDetails> getCardDetails(Long columnId, Long cardId) {
        List<CardDetails> cardDetailsList = cardRepository.findCardDetailsByColumnId(columnId,
            cardId);
        List<CardDetails> cardDetailsWithCommentsList = new ArrayList<>();
        for (CardDetails cardDetails : cardDetailsList) {
            List<CommentResponse> comments = commentService.getComments(cardId);
            CardDetails cardDetailsWithComments = new CardDetails(cardDetails, comments);
            cardDetailsWithCommentsList.add(cardDetailsWithComments);
        }
        return cardDetailsWithCommentsList;
    }

    //카드 업데이트
    @Transactional
    public Card updateCard(Long columnId, Long cardId,
        CardUpdateRequest updateRequest, User user) {
        findColumn(columnId);
        Card card = findCard(cardId);

        if (updateRequest.getCardName() != null) {
            card.updateCardName(updateRequest.getCardName());
        }
        if (updateRequest.getDescription() != null) {
            card.updateDescription(updateRequest.getDescription());
        }
        if (updateRequest.getColor() != null) {
            card.updateColor(updateRequest.getColor());
        }
        if (updateRequest.getDueDate() != null) {
            card.updateDueDate(updateRequest.getDueDate());
        }

        if (updateRequest.getWorkerId() != null) {
            User worker = userRepository.findById(updateRequest.getWorkerId())
                .orElseThrow(() -> new NoSuchElementException("해당 ID에 해당하는 작업자를 찾을 수 없습니다"));
            Worker newWorker = new Worker(worker);
            card.addWorker(newWorker.getUser());
        }

        // 작업자 제거
        if (updateRequest.getRemoveWorkerId() != null) {
            Long removeWorkerId = updateRequest.getRemoveWorkerId();
            removeWorkerFromCard(card, removeWorkerId);
        }
        cardRepository.save(card);

        return card;
    }

    @Transactional
    public void removeWorkerFromCard(Card card, Long removeWorkerId) {

        Worker removeWorker = workerRepository.findById(removeWorkerId)
            .orElseThrow(() -> new NoSuchElementException("해당 ID에 해당하는 작업자를 찾을 수 없습니다"));

        if (!removeWorker.getUser().getId().equals(card.getUser().getId())) {
            throw new NoSuchElementException("해당 작업자가 카드에 존재하지 않습니다");
        }

        workerRepository.delete(removeWorker);
    }

    //카드 삭제
    @Transactional
    public void deleteCard(Long columnId, Long cardId, User user) {
        findColumn(columnId);

        Card card = findCard(cardId);

        cardRepository.delete(card);
    }

    @Transactional
    public CardResponse updateCardSequence(Long columnId, Long cardId,
        CardMoveRequest request) {
        Card card = findCard(cardId);

        Long between = (request.getPrevSequence() + request.getNextSequence()) / 2;

        if (between.equals(request.getPrevSequence())) {
            Card prevCard = cardRepository.findBySequence(columnId, request.getPrevSequence());

            prevCard.setSequence(card.getSequence());
        } else if (between.equals(request.getNextSequence())) {
            Card nextCard = cardRepository.findBySequence(columnId, request.getNextSequence());

            nextCard.setSequence(card.getSequence());
        }

        card.setSequence(between);
        return CardResponse.builder()
            .cardName(card.getCardName())
            .sequence(card.getSequence())
            .build();
    }

    public void moveCardToColumn(Long columnId, Long cardId, Long targetColumnId) {
        Card card = findCard(cardId);
        Columns targetColumn = findColumn(targetColumnId);

        card.setColumn(targetColumn);
        cardRepository.save(card);
    }

    private Columns findColumn(Long columnId) {
        return columnRepository.findById(columnId)
            .orElseThrow(() -> new NoSuchElementException("컬럼을 찾을 수 없습니다."));
    }

    private Card findCard(Long cardId) {
        return cardRepository.findById(cardId)
            .orElseThrow(() -> new NoSuchElementException("카드를 찾을 수 없습니다."));
    }
}