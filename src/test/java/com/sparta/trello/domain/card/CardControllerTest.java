package com.sparta.trello.domain.card;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.trello.MockSpringSecurityFilter;
import com.sparta.trello.domain.card.controller.CardController;
import com.sparta.trello.domain.card.dto.CardMoveRequest;
import com.sparta.trello.domain.card.dto.CardRequest;
import com.sparta.trello.domain.card.dto.CardUpdateRequest;
import com.sparta.trello.domain.card.service.CardService;
import com.sparta.trello.domain.column.entity.Columns;
import com.sparta.trello.domain.user.entity.User;
import com.sparta.trello.global.config.WebSecurityConfig;
import com.sparta.trello.global.security.UserDetailsImpl;
import java.security.Principal;
import java.time.LocalDateTime;
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
    controllers = {CardController.class},
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = WebSecurityConfig.class
        )
    }
)
public class CardControllerTest {

    private MockMvc mockMvc;
    private Principal principal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    CardService cardService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity(new MockSpringSecurityFilter()))
            .build();

        mockSetup();
    }
    User user;
    Columns columns;

    private void mockSetup() {
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
    @DisplayName("Create Card")
    void createCard() throws Exception {
        // given
        String cardName = "카드 생성 테스트";
        Long sequence = 2000L;
        CardRequest request = CardRequest.builder().cardName(cardName).sequence(sequence).build();
        String cardJson = objectMapper.writeValueAsString(request);

        // when - then
        mockMvc.perform(post("/api/v1/boards/100/columns/500/cards").content(cardJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).principal(principal))
            .andExpect(status().isCreated())
            .andDo(print());
    }

    @Test
    @DisplayName("Update Card")
    void updateCard() throws Exception {
        String cardName = "카드 수정";
        String description = "카드 내용";
        String color = "카드 색깔";
        Long workerId = 1000L;
        LocalDateTime dueDate = LocalDateTime.now();
        CardUpdateRequest request = CardUpdateRequest.builder()
            .cardName(cardName).description(description).color(color).workerId(workerId).dueDate(dueDate).build();

        String cardJson = objectMapper.writeValueAsString(request);

        // when - then
        mockMvc.perform(patch("/api/v1/boards/100/columns/500/cards/1000").content(cardJson)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON).principal(principal))
            .andExpect(status().isNoContent())
            .andDo(print());
    }

    @Test
    @DisplayName("Get Card Summary")
    void getCardSummary() throws Exception {
        mockMvc.perform(get("/api/v1/boards/100/columns/500/cards"))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("Get Card Details")
    void getCardDetails() throws Exception {
        mockMvc.perform(get("/api/v1/boards/100/columns/500/cards/1000"))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("Delete Card")
    void deleteCard() throws Exception {
        mockMvc.perform(delete("/api/v1/boards/100/columns/500/cards/1000")
            .accept(MediaType.APPLICATION_JSON).principal(principal))
            .andExpect(status().isNoContent())
            .andDo(print());
    }

    @Test
    @DisplayName("Update Card Sequence")
    void updateCardSequence() throws Exception {
        Long prevSequence = 2000L, nextSequence = 5000L;
        CardMoveRequest request = CardMoveRequest.builder()
            .prevSequence(prevSequence).nextSequence(nextSequence).build();

        String cardJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(patch("/api/v1/boards/100/columns/500/cards/1000/sequence").content(cardJson)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON).principal(principal))
            .andExpect(status().isNoContent())
            .andDo(print());
    }

    @Test
    @DisplayName("Move Card To Column")
    void moveCardToColumn() throws Exception {
        mockMvc.perform(patch("/api/v1/boards/100/columns/500/cards/1000/move")
                .param("targetColumnId", "2")
            .accept(MediaType.APPLICATION_JSON).principal(principal))
            .andExpect(status().isNoContent())
            .andDo(print());
    }

}
