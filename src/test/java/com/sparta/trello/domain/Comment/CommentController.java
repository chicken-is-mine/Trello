package com.sparta.trello.domain.Comment;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.trello.MockSpringSecurityFilter;
import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.board.entity.BoardColorEnum;
import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.column.entity.Columns;
import com.sparta.trello.domain.comment.dto.CommentRequest;
import com.sparta.trello.domain.comment.service.CommentService;
import com.sparta.trello.domain.user.entity.User;
import com.sparta.trello.global.config.WebSecurityConfig;
import com.sparta.trello.global.security.UserDetailsImpl;
import java.security.Principal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(
    controllers = {com.sparta.trello.domain.comment.controller.CommentController.class},
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = WebSecurityConfig.class
        )
    }
)
public class CommentController {

    private MockMvc mvc;
    private Principal principal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CommentService commentService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity(new MockSpringSecurityFilter()))
            .build();

        mockSetup();
    }

    User user;
    Board board;
    Columns columns;
    Card card;

    private void mockSetup() {
        Long userId = 1L;
        String email = "aaa@naver.com";
        String userName = "사용자1";
        String password = "aaa111!";
        user = User.builder().id(userId).email(email).username(userName).password(password).build();
        board = Board.builder().boardId(1l).boardName("새보드").description("보드 설명")
            .color(BoardColorEnum.BLACK).user(user).build();
        columns = Columns.builder().columnId(1L).columnName("새 컬럼").build();
        card = Card.builder().cardId(1l).cardName("새 카드").user(user).build();
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        principal = new UsernamePasswordAuthenticationToken(userDetails, "",
            userDetails.getAuthorities());
    }

    @Test
    @DisplayName("댓글 생성 API 테스트")
    void createCommentTest() throws Exception {
        // given
        CommentRequest request = new CommentRequest("새 댓글");

        // when-then
        mvc.perform(post("/api/v1/boards/1/cards/1/comments")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(principal)
            )
            .andExpect(status().isCreated())
            .andDo(print());
    }

    @Test
    @DisplayName("댓글 수정 API 테스트")
    void updateComment() throws Exception {
        // given
        CommentRequest request = new CommentRequest("수정된 댓글");

        // when-then
        mvc.perform(patch("/api/v1/boards/1/cards/1/comments/1")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(principal)
            )
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("댓글 수정 API 테스트")
    void DeleteComment() throws Exception {

        // when-then
        mvc.perform(delete("/api/v1/boards/1/cards/1/comments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(principal)
            )
            .andExpect(status().isOk())
            .andDo(print());
    }


}
