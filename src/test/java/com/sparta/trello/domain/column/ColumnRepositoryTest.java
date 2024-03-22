package com.sparta.trello.domain.column;

import static com.sparta.trello.domain.column.entity.QColumns.columns;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.entity.BoardColorEnum;
import com.sparta.trello.domain.board.repository.BoardRepository;
import com.sparta.trello.domain.column.entity.Columns;
import com.sparta.trello.domain.column.repository.ColumnRepository;
import com.sparta.trello.domain.user.entity.User;
import com.sparta.trello.domain.user.repository.UserRepository;
import com.sparta.trello.global.config.QueryDslConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(QueryDslConfig.class)
public class ColumnRepositoryTest {

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

        user2 = User.builder().email("abc456@naver.com").password("abc!2345")
            .username("abc456")
            .profile("").build();
        userRepository.save(user2);

        board2 = Board.builder().boardName("보드2").color(BoardColorEnum.BLACK).description("")
            .user(user2)
            .build();
        boardRepository.save(board2);
    }

    @Test
    public void findBySequence() {
        // given
        Long boardId = board1.getBoardId();
        columnRepository.save(Columns.builder().columnName("컬럼 1").sequence(1000L).board(board1).build());
        columnRepository.save(Columns.builder().columnName("컬럼 2").sequence(1000L).board(board2).build());
        Long sequence = 1000L;

        // when
        Columns column = queryFactory
            .selectFrom(columns)
            .where(columns.sequence.eq(sequence), columns.board.boardId.eq(boardId))
            .fetchFirst();

        // then
        if(column != null) {
            System.out.println(column);
        }
    }

    @Test
    @DisplayName("순서 정렬 조회")
    public void findAllByBoardIdOrderBySequence() {
        // given
        Long boardId = board1.getBoardId();
        generateColumns();

        // when
        long startTime = System.currentTimeMillis();
        System.out.println("\n======= QueryDSL =======");
        List<Columns> queryDslColumns = queryFactory
            .selectFrom(columns)
            .where(columns.board.boardId.eq(boardId))
            .orderBy(columns.sequence.asc())
            .fetch();
        long endTime = System.currentTimeMillis();
        long time = endTime - startTime;

        // then
        System.out.println(time + "ms");
        for(Columns c : queryDslColumns) {
            System.out.println(c);
        }
    }

    private void generateColumns() {
        List<Columns> columnsList = new ArrayList<>();
        Long randomSequence;
        for (int i = 0; i < 100; i++) {
            randomSequence = new Random().nextLong(100000L);
            Columns columns = Columns.builder().columnId(100L + (long) i).columnName("컬럼 " + i)
                .sequence(randomSequence).board(board1).build();
            columnsList.add(columns);
        }

        columnRepository.saveAll(columnsList);

        columnsList = new ArrayList<>();
        for (int i = 100; i < 200; i++) {
            randomSequence = new Random().nextLong(100000L);
            Columns columns = Columns.builder().columnId(100L + (long) i).columnName("컬럼 " + i)
                .sequence(randomSequence).board(board1).build();
            columnsList.add(columns);
        }

        columnRepository.saveAll(columnsList);
    }
}
