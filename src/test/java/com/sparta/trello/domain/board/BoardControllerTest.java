package com.sparta.trello.domain.board;


import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.trello.MockSpringSecurityFilter;
import com.sparta.trello.domain.board.controller.BoardController;
import com.sparta.trello.domain.board.dto.BoardRequest;
import com.sparta.trello.domain.board.dto.InviteUserRequest;
import com.sparta.trello.domain.board.entity.BoardColorEnum;
import com.sparta.trello.domain.board.service.BoardService;
import com.sparta.trello.domain.user.entity.User;
import com.sparta.trello.global.config.WebSecurityConfig;
import com.sparta.trello.global.security.UserDetailsImpl;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;
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

@WebMvcTest(controllers = {BoardController.class},
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = WebSecurityConfig.class
        )
    })
public class BoardControllerTest {

    private MockMvc mockMvc;
    private Principal principal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    BoardService boardService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity(new MockSpringSecurityFilter()))
            .build();

        mockSetup();
    }

    User user;

    private void mockSetup() {
        // Mock 테스트 유저 생성
        Long userId = 100L;
        String email = "abc123@naver.com";
        String userName = "abc123";
        String password = "abc12345";
        user = User.builder().id(userId).email(email).username(userName).password(password).build();
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        principal = new UsernamePasswordAuthenticationToken(userDetails, "",
            userDetails.getAuthorities());
    }

    @Test
    @DisplayName("보드 생성 테스트")
    void createBoard() throws Exception {
        //given
        String boardName = "Board";
        String description = "Description";
        BoardColorEnum color = BoardColorEnum.fromValue(1);

        BoardRequest boardRequest = BoardRequest.builder().boardName(boardName)
            .description(description)
            .color(color.getValue())
            .build();

        String postInfo = objectMapper.writeValueAsString(boardRequest);

        //when-then
        mockMvc.perform(post("/api/v1/boards").content(postInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(principal))
            .andExpect(status().isCreated())
            .andDo(print());
    }

    @Test
    @DisplayName("보드 수정 테스트")
    void upDateBoard() throws Exception {

        Long boardId = 100L;
        String boardName = "newBoard";
        String description = "newDescription";
        BoardColorEnum color = BoardColorEnum.fromValue(3);

        BoardRequest boardRequest = BoardRequest.builder().boardName(boardName)
            .description(description)
            .color(color.getValue())
            .build();

        String postInfo = objectMapper.writeValueAsString(boardRequest);

        //when-then
        mockMvc.perform(patch("/api/v1/boards/{boardId}", boardId).content(postInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).principal(principal))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("보드 삭제 테스트")
    void deleteBoard() throws Exception {
        //given
        Long boardId = 100L;

        //when-then
        mockMvc.perform(delete("/api/v1/boards/{boardId}", boardId)
                .accept(MediaType.APPLICATION_JSON)
                .principal(principal))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("유저 초대 테스트")
    void inviteUser() throws Exception {
        // given
        Long boardId = 100L;
        List<Long> userIds = Arrays.asList(200L, 300L);

        InviteUserRequest inviteUserRequest = new InviteUserRequest(userIds);

        String postInfo = objectMapper.writeValueAsString(inviteUserRequest);

        // when-then
        mockMvc.perform(post("/api/v1/boards/{boardId}/invite", boardId)
                .content(postInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(principal))
            .andExpect(status().isOk())
            .andDo(print());
    }
}
