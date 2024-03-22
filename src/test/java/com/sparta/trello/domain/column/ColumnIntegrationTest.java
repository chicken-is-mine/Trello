package com.sparta.trello.domain.column;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.trello.MockSpringSecurityFilter;
import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.entity.BoardColorEnum;
import com.sparta.trello.domain.board.entity.BoardRoleEnum;
import com.sparta.trello.domain.board.entity.BoardUser;
import com.sparta.trello.domain.board.repository.BoardRepository;
import com.sparta.trello.domain.board.repository.BoardUserJpaRepository;
import com.sparta.trello.domain.board.service.BoardService;
import com.sparta.trello.domain.column.dto.CreateColumnRequest;
import com.sparta.trello.domain.column.dto.ModifyColumnNameRequest;
import com.sparta.trello.domain.column.dto.ModifyColumnSequenceRequest;
import com.sparta.trello.domain.column.entity.Columns;
import com.sparta.trello.domain.column.repository.ColumnRepository;
import com.sparta.trello.domain.column.service.ColumnService;
import com.sparta.trello.domain.user.entity.User;
import com.sparta.trello.domain.user.repository.UserRepository;
import com.sparta.trello.domain.user.service.UserService;
import com.sparta.trello.global.security.UserDetailsImpl;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2, replace = AutoConfigureTestDatabase.Replace.ANY)
@TestPropertySource("classpath:application-test.properties")
public class ColumnIntegrationTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardUserJpaRepository boardUserJpaRepository;

    @Autowired
    ColumnRepository columnRepository;

    private MockMvc mockMvc;

    Principal principal;
    User user;
    Board board;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity(new MockSpringSecurityFilter()))
            .build();

        mockSetup();
    }

    private void mockSetup() {
        // 테스트 유저 생성
        String email = "test123@naver.com";
        String userName = "test";
        String password = "abc12345";
        user = userRepository.save(
            User.builder().email(email).username(userName).password(password).profile("").build());
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        principal = new UsernamePasswordAuthenticationToken(userDetails, "",
            userDetails.getAuthorities());

        // 테스트 보드 생성
        String boardName = "보드";
        BoardColorEnum boardColorEnum = BoardColorEnum.BLACK;
        board = boardRepository.save(
            Board.builder().boardName(boardName).color(boardColorEnum).description("").user(user)
                .build());

        // 워크스페이스 설정
        boardUserJpaRepository.save(new BoardUser(board, user, BoardRoleEnum.OWNER));
    }

    @Test
    public void createColumn() throws Exception {
        // given
        CreateColumnRequest request = CreateColumnRequest.builder().columnName("컬럼").sequence(1000L)
            .build();

        String postInfo = objectMapper.writeValueAsString(request);

        // when-then
        mockMvc.perform(
                post("/api/v1/boards/{boardId}/columns", board.getBoardId())
                    .content(postInfo)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .principal(principal))
            .andExpect(status().isCreated())
            .andDo(print());
    }

    @Test
    public void modifyColumnName() throws Exception {
        // given
        Columns columns = columnRepository.save(
            Columns.builder().columnName("컬럼").sequence(1000L).board(board).build());
        String name = "컬럼 수정";
        ModifyColumnNameRequest request = ModifyColumnNameRequest.builder().columnName(name)
            .build();

        String postInfo = objectMapper.writeValueAsString(request);

        // when-then
        mockMvc.perform(
                patch("/api/v1/boards/{boardId}/columns/{columnId}", board.getBoardId(),
                    columns.getColumnId())
                    .content(postInfo)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .principal(principal))
            .andExpect(status().isNoContent())
            .andDo(print());
    }

    @Test
    public void modifyColumnSequence_Success() throws Exception {
        // given
        Long sequence = 1000L, prevSequence = 2000L, nextSequence = 3000L;
        Columns columns = columnRepository.save(
            Columns.builder().columnName("컬럼").sequence(sequence).board(board).build());
        ModifyColumnSequenceRequest request = ModifyColumnSequenceRequest.builder()
            .prevSequence(prevSequence).nextSequence(nextSequence).build();

        String postInfo = objectMapper.writeValueAsString(request);

        // when-then
        mockMvc.perform(
                patch("/api/v1/boards/{boardId}/columns/{columnId}/sequence", board.getBoardId(),
                    columns.getColumnId())
                    .content(postInfo)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .principal(principal))
            .andExpect(status().isNoContent())
            .andExpect(jsonPath("$.data.sequence").value((prevSequence + nextSequence) / 2))
            .andDo(print());
    }

    @Test
    public void getColumnsOrderBySequence() throws Exception {
        // given
        List<Columns> columnsList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            columnsList.add(
                Columns.builder().columnName("컬럼 " + i + 1).sequence(new Random().nextLong(10000L))
                    .board(board).build());
        }
        columnsList = columnRepository.saveAll(columnsList);

        // when-then
        mockMvc.perform(get("/api/v1/boards/{boardId}/columns", board.getBoardId())
                .accept(MediaType.APPLICATION_JSON).principal(principal))
            .andExpect(status().isOk())
            .andDo(print());
    }
}
