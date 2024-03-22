package com.sparta.trello.domain.column;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.trello.MockSpringSecurityFilter;
import com.sparta.trello.domain.column.controller.ColumnController;
import com.sparta.trello.domain.column.dto.CreateColumnRequest;
import com.sparta.trello.domain.column.dto.ModifyColumnNameRequest;
import com.sparta.trello.domain.column.dto.ModifyColumnSequenceRequest;
import com.sparta.trello.domain.column.service.ColumnService;
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
    controllers = {ColumnController.class},
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = WebSecurityConfig.class
        )
    }
)
public class ColumnControllerTest {

    private MockMvc mockMvc;
    private Principal principal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    ColumnService columnService;

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
    @DisplayName("Create Column")
    void createColumn() throws Exception {
        // given
        String columnName = "컬럼";
        Long sequence = 1000L;
        CreateColumnRequest request = CreateColumnRequest.builder().columnName(columnName)
            .sequence(sequence).build();

        String postInfo = objectMapper.writeValueAsString(request);

        // when-then
        mockMvc.perform(post("/api/v1/boards/100/columns").content(postInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).principal(principal))
            .andExpect(status().isCreated())
            .andDo(print());
    }

    @Test
    void modifyColumnName() throws Exception {
        // given
        String columnName = "컬럼 수정";
        ModifyColumnNameRequest request = ModifyColumnNameRequest.builder().columnName(columnName)
            .build();

        String postInfo = objectMapper.writeValueAsString(request);

        // when-then
        mockMvc.perform(patch("/api/v1/boards/100/columns/100").content(postInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).principal(principal))
            .andExpect(status().isNoContent())
            .andDo(print());
    }

    @Test
    void modifyColumnSequence() throws Exception {
        // given
        Long prevSequence = 2000L, nextSequence = 3000L;
        ModifyColumnSequenceRequest request = ModifyColumnSequenceRequest.builder()
            .prevSequence(prevSequence).nextSequence(nextSequence).build();

        String postInfo = objectMapper.writeValueAsString(request);

        // when-then
        mockMvc.perform(patch("/api/v1/boards/100/columns/100/sequence").content(postInfo)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).principal(principal))
            .andExpect(status().isNoContent())
            .andDo(print());
    }

    @Test
    void deleteColumn() throws Exception {
        // given

        // when-then
        mockMvc.perform(delete("/api/v1/boards/100/columns/100")
                .accept(MediaType.APPLICATION_JSON).principal(principal))
            .andExpect(status().isNoContent())
            .andDo(print());
    }

    @Test
    void getColumnOrderBySequence() throws Exception {
        // given

        // when-then
        mockMvc.perform(get("/api/v1/boards/100/columns")
                .accept(MediaType.APPLICATION_JSON).principal(principal))
            .andExpect(status().isOk())
            .andDo(print());
    }
}
