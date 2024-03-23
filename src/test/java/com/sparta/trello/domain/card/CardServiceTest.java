package com.sparta.trello.domain.card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.never;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;

import com.sparta.trello.domain.card.dto.CardRequest;
import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.card.repository.CardRepository;
import com.sparta.trello.domain.card.repository.WorkerRepository;
import com.sparta.trello.domain.card.service.CardService;
import com.sparta.trello.domain.column.entity.Columns;
import com.sparta.trello.domain.column.repository.ColumnRepository;
import com.sparta.trello.domain.comment.service.CommentService;
import com.sparta.trello.domain.user.entity.User;
import com.sparta.trello.domain.user.repository.UserRepository;
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

}
