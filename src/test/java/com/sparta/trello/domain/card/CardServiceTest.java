package com.sparta.trello.domain.card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.trello.domain.card.dto.CardDetails;
import com.sparta.trello.domain.card.dto.CardMoveRequest;
import com.sparta.trello.domain.card.dto.CardRequest;
import com.sparta.trello.domain.card.dto.CardResponse;
import com.sparta.trello.domain.card.dto.CardSummary;
import com.sparta.trello.domain.card.dto.CardUpdateRequest;
import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.card.repository.CardRepository;
import com.sparta.trello.domain.card.repository.WorkerRepository;
import com.sparta.trello.domain.card.service.CardService;
import com.sparta.trello.domain.column.entity.Columns;
import com.sparta.trello.domain.column.repository.ColumnRepository;
import com.sparta.trello.domain.comment.dto.CommentResponse;
import com.sparta.trello.domain.comment.service.CommentService;
import com.sparta.trello.domain.user.entity.User;
import com.sparta.trello.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    WorkerRepository workerRepository;
    @Mock
    ColumnRepository columnRepository;
    @Mock
    CardRepository cardRepository;
    @Mock
    CommentService commentService;

    CardService cardService;

    @BeforeEach
    void setUp() {
        cardService = new CardService(cardRepository, columnRepository, userRepository,
            workerRepository, commentService);
    }

    @Test
    void createCard_Success() {
        Long columnId = 100L;
        Columns columns = Columns.builder().columnId(columnId).build();
        given(columnRepository.findById(columnId)).willReturn(Optional.of(columns));

        Card card = Card.builder().cardId(100L).cardName("카드 테스트1").sequence(2000L)
            .column(columns).build();
        User user = User.builder().id(100L).username("유저").email("abc@gmail.com").build();
        CardRequest request = new CardRequest(card.getCardName(), card.getSequence());

        given(cardRepository.save(any(Card.class))).willReturn(card);

        // when
        cardService.createCard(columnId, request, user);

        // then
        verify(columnRepository, times(1)).findById(columnId);
        verify(cardRepository, times(1)).save(any(Card.class));
    }

    @Test
    void creatCard_Failure() {
        // given
        Long columnId = 100L;
        CardRequest request = new CardRequest("카드 테스트", 2000L);

        given(columnRepository.findById(columnId)).willReturn(Optional.empty());

        // when & then

        assertThrows(
            NoSuchElementException.class, () -> cardService.createCard(columnId, request, null));
        verify(columnRepository, times(1)).findById(columnId);
        verify(cardRepository, never()).save(any(Card.class));
    }

    @Test
    void updateCard_Success() {
        // given
        Columns columns = Columns.builder().columnId(100L).build();
        Card card = Card.builder().cardId(200L).cardName("카드 테스트").sequence(1000L).build();
        User user = User.builder().id(100L).username("유저").email("abc@gmail.com").build();
        given(columnRepository.findById(100L)).willReturn(Optional.of(columns));
        given(cardRepository.findById(200L)).willReturn(Optional.of(card));
        String name = "카드 수정 테스트";

        // when
        Card card1 = cardService.updateCard(100L, 200L,
            CardUpdateRequest.builder().cardName(name).build(), user);

        // then
        assertEquals(card.getCardName(), card1.getCardName());
    }

    @Test
    void updateCardSequence() {
        // given
        Card card = Card.builder().cardId(100L).cardName("카드").sequence(1000L).build();
        given(cardRepository.findById(100L)).willReturn(Optional.of(card));
        Long prevSequence = 3000L, nextSequence = 5000L;
        Long between = (prevSequence + nextSequence) / 2;
        CardMoveRequest request = CardMoveRequest.builder()
            .prevSequence(prevSequence).nextSequence(nextSequence).build();

        // when
        CardResponse response = cardService.updateCardSequence(100L, 100L, request);

        // then
        assertEquals(between, response.getSequence());
    }

    @Test
    void updateCardSequence_EqualBetween() {
        Long sequence = 3000L;
        Card card = Card.builder().cardId(100L).cardName("카드").sequence(sequence).build();
        given(cardRepository.findById(100L)).willReturn(Optional.of(card));
        Long prevSequence = 3000L, nextSequence = 3001L;
        Long between = (prevSequence + nextSequence) / 2;
        CardMoveRequest request = CardMoveRequest.builder()
            .prevSequence(prevSequence).nextSequence(nextSequence).build();

        Card prevCard = Card.builder().cardId(101L).cardName("이전 카드")
            .sequence(prevSequence).build();
        given(cardRepository.findBySequence(anyLong(), anyLong())).willReturn(prevCard);

        CardResponse response = cardService.updateCardSequence(0L, 100L, request);

        assertEquals(between, response.getSequence());
        assertEquals(sequence, response.getSequence());
    }

    @Test
    void GetCardSummary() {
        // given
        Long columnId = 100L;
        List<CardSummary> expectedSummary = new ArrayList<>();

        expectedSummary.add(
            CardSummary.builder().cardId(1L).cardName("Card1").commentCount(2L).build());
        expectedSummary.add(
            CardSummary.builder().cardId(2L).cardName("Card2").commentCount(3L).build());

        when(cardRepository.findCardsSummaryByColumnId(columnId)).thenReturn(expectedSummary);

        // when
        List<CardSummary> actualSummary = cardService.getCardSummary(columnId);

        // then
        assertEquals(expectedSummary.size(), actualSummary.size());
        for (int i = 0; i < expectedSummary.size(); i++) {
            assertEquals(expectedSummary.get(i).getCardId(), actualSummary.get(i).getCardId());
            assertEquals(expectedSummary.get(i).getCardName(), actualSummary.get(i).getCardName());
            assertEquals(expectedSummary.get(i).getCommentCount(),
                actualSummary.get(i).getCommentCount());
        }
        verify(cardRepository, times(1)).findCardsSummaryByColumnId(columnId);
    }

    @Test
    void GetCardDetails() {
        // given
        Long columnId = 100L;
        Long cardId = 200L;

        List<CardDetails> expectedDetailsList = new ArrayList<>();
        expectedDetailsList.add(
            CardDetails.builder()
                .cardId(cardId)
                .cardName("Card1")
                .description("Description1")
                .color("Red")
                .dueDate(LocalDateTime.now())
                .workers(new ArrayList<>())
                .comments(new ArrayList<>())
                .build()
        );

        List<CommentResponse> mockComments = new ArrayList<>();
        mockComments.add(new CommentResponse("유저1", "댓글1"));
        mockComments.add(new CommentResponse("유저2", "댓글2"));

        List<CommentResponse> expectedComments = new ArrayList<>(mockComments);

        when(cardRepository.findCardDetailsByColumnId(columnId, cardId)).thenReturn(
            expectedDetailsList);
        when(commentService.getComments(cardId)).thenReturn(mockComments);

        // when
        List<CardDetails> actualDetailsList = cardService.getCardDetails(columnId, cardId);

        // then
        assertEquals(expectedDetailsList.size(), actualDetailsList.size());

        for (int i = 0; i < expectedDetailsList.size(); i++) {
            CardDetails expectedDetails = expectedDetailsList.get(i);
            CardDetails actualDetails = actualDetailsList.get(i);

            assertEquals(expectedDetails.getCardId(), actualDetails.getCardId());
            assertEquals(expectedDetails.getCardName(), actualDetails.getCardName());
            assertEquals(expectedDetails.getDescription(), actualDetails.getDescription());
            assertEquals(expectedDetails.getColor(), actualDetails.getColor());
            assertEquals(expectedDetails.getDueDate(), actualDetails.getDueDate());
            assertEquals(expectedDetails.getWorkers(), actualDetails.getWorkers());
            assertEquals(expectedComments.size(), actualDetails.getComments().size());
        }

        verify(cardRepository, times(1)).findCardDetailsByColumnId(columnId, cardId);
        verify(commentService, times(1)).getComments(cardId);
    }

    @Test
    void MoveCardToColumn() {
        // given
        Long columnId = 100L;
        Long cardId = 200L;
        Long targetColumnId = 300L;

        Card card = new Card();
        Columns targetColumn = new Columns();

        when(cardRepository.findById(cardId)).thenReturn(Optional.of(card));
        when(columnRepository.findById(targetColumnId)).thenReturn(Optional.of(targetColumn));

        // when
        cardService.moveCardToColumn(columnId, cardId, targetColumnId);

        // then
        verify(cardRepository, times(1)).findById(cardId);
        verify(columnRepository, times(1)).findById(targetColumnId);
        verify(cardRepository, times(1)).save(card);

        assertEquals(targetColumn, card.getColumn());
    }
}
