package com.sparta.trello.domain.card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.EntityPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.entity.BoardColorEnum;
import com.sparta.trello.domain.board.repository.BoardRepository;
import com.sparta.trello.domain.card.dto.CardSummary;
import com.sparta.trello.domain.card.repository.CardRepository;
import com.sparta.trello.domain.column.entity.Columns;
import com.sparta.trello.domain.column.repository.ColumnRepository;
import com.sparta.trello.domain.user.entity.User;
import com.sparta.trello.domain.user.repository.UserRepository;
import com.sparta.trello.global.config.QueryDslConfig;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(QueryDslConfig.class)
public class CardRepositoryTest {

    @Autowired
    CardRepository cardRepository;
    @Autowired
    ColumnRepository columnRepository;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    JPAQueryFactory queryFactory;

    private User user1, user2;
    private Board board1, board2;
    private Columns column1, column2;

    @BeforeEach
    void setUp() {
        user1 = User.builder().email("abc123@naver.com").password("abc!2345")
            .username("abc123")
            .profile("").build();
        userRepository.save(user1);

        board1 = Board.builder().boardName("보드1").color(BoardColorEnum.BLACK).description("")
            .user(user1)
            .build();
        boardRepository.save(board1);

        column1 = Columns.builder().columnName("컬럼1").sequence(1000L).board(board1).build();
        columnRepository.save(column1);

        user2 = User.builder().email("abc456@naver.com").password("abc!2345")
            .username("abc456")
            .profile("").build();
        userRepository.save(user2);

        board2 = Board.builder().boardName("보드2").color(BoardColorEnum.BLACK).description("")
            .user(user2)
            .build();
        boardRepository.save(board2);

        column2 = Columns.builder().columnName("컬럼2").sequence(3000L).board(board2).build();
        columnRepository.save(column2);
    }

    @Test
    void testGetCardDetails() {
        // given
        Long columnId = column1.getColumnId();

        JPAQuery mockQuery = mock(JPAQuery.class);

        Tuple tuple1 = mock(Tuple.class);
        when(tuple1.get(0, Long.class)).thenReturn(1L);
        when(tuple1.get(1, String.class)).thenReturn("카드1");
        when(tuple1.get(2, String.class)).thenReturn("유저1");
        when(tuple1.get(3, Long.class)).thenReturn(2L);

        Tuple tuple2 = mock(Tuple.class);
        when(tuple2.get(0, Long.class)).thenReturn(2L);
        when(tuple2.get(1, String.class)).thenReturn("카드2");
        when(tuple2.get(2, String.class)).thenReturn("유저2");
        when(tuple2.get(3, Long.class)).thenReturn(1L);

        List<Tuple> mockResults = new ArrayList<>();
        mockResults.add(tuple1);
        mockResults.add(tuple2);

        when(mockQuery.fetch())
            .thenReturn(mockResults);

        if (Mockito.mockingDetails(queryFactory).isMock()) {

            when(queryFactory.select(any(), any(), any(), any())).thenReturn(mockQuery);
            when(queryFactory.from(any(EntityPath.class)))
                .thenReturn(mockQuery);
            when(mockQuery.leftJoin(any(EntityPath.class)))
                .thenReturn(mockQuery);
            when(mockQuery.where(any(com.querydsl.core.types.Predicate.class)))
                .thenReturn(mockQuery);

            // when
            List<CardSummary> cardSummaryList = cardRepository.findCardsSummaryByColumnId(columnId);

            // then
            assertNotNull(cardSummaryList);
            assertEquals(2, cardSummaryList.size());

            CardSummary firstCardSummary = cardSummaryList.get(0);
            assertEquals(1L, firstCardSummary.getCardId());
            assertEquals("카드1", firstCardSummary.getCardName());
            assertEquals(2L, firstCardSummary.getCommentCount());
            assertEquals(1, firstCardSummary.getWorkers().size());
            assertEquals("유저1", firstCardSummary.getWorkers().get(0).getUsername());

            CardSummary secondCardSummary = cardSummaryList.get(1);
            assertEquals(2L, secondCardSummary.getCardId());
            assertEquals("카드2", secondCardSummary.getCardName());
            assertEquals(1L, secondCardSummary.getCommentCount());
            assertEquals(1, secondCardSummary.getWorkers().size());
            assertEquals("유저2", secondCardSummary.getWorkers().get(0).getUsername());
        }

    }
}
