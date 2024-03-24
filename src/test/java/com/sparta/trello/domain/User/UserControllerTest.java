package com.sparta.trello.domain.User;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import com.sparta.trello.domain.user.controller.UserController;
import com.sparta.trello.domain.user.dto.InfoRequest;
import com.sparta.trello.domain.user.dto.SignupRequest;
import com.sparta.trello.domain.user.entity.User;
import com.sparta.trello.domain.user.service.UserService;
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
    controllers = {UserController.class},
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = WebSecurityConfig.class
        )
    }
)
public class UserControllerTest {

    private MockMvc mvc;
    private Principal principal;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    UserService userService;

    User user;
    Board board;
    Columns columns;
    Card card;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity(new MockSpringSecurityFilter()))
            .build();

        mockSetup();
    }

    @BeforeEach
    void mockSetup() {
        Long userId = 1L;
        String email = "Aaa@naver.com";
        String userName = "사용자1";
        String password = "Aaa111!";
        user = User.builder().id(userId).email(email).username(userName).password(password).build();
        board = Board.builder().boardId(1l).boardName("새보드").description("보드 설명").color(
            BoardColorEnum.BLACK).user(user).build();
        columns = Columns.builder().columnId(1L).columnName("새 컬럼").build();
        card = Card.builder().cardId(1l).cardName("새 카드").user(user).build();
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        principal = new UsernamePasswordAuthenticationToken(userDetails, "",
            userDetails.getAuthorities());
    }

    @Test
    @DisplayName("회원가입 API 테스트")
    void signupTest() throws Exception {
        // given
        SignupRequest signupRequest = new SignupRequest("aaa@naver.com", "Aaa1234!", "사용자1",
            "한줄 소개");

        // when
        mvc.perform(post("/api/v1/users/signup")
                .content(objectMapper.writeValueAsString(signupRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(principal)
            )
            .andExpect(status().isCreated())
            .andDo(print());
    }


    @Test
    @DisplayName("마이페이지 조회 API 테스트")
    void showProfileTest() throws Exception {
        // when
        mvc.perform(get("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(principal)
            )
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("사용자 정보 수정 API 테스트")
    void updateProfileTest() throws Exception {

        // given
        InfoRequest infoRequest = new InfoRequest("수정 유저이름", "수정 프로필");

        // when
        mvc.perform(patch("/api/v1/users")
                .content(objectMapper.writeValueAsString(infoRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .principal(principal)
            )
            .andExpect(status().isOk())
            .andDo(print());
    }
}
