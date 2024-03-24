package com.sparta.trello.domain.User;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.trello.domain.board.dto.BoardInfo;
import com.sparta.trello.domain.board.repository.BoardRepository;
import com.sparta.trello.domain.card.dto.CardInfo;
import com.sparta.trello.domain.card.repository.CardRepository;
import com.sparta.trello.domain.user.dto.InfoRequest;
import com.sparta.trello.domain.user.dto.InfoResponse;
import com.sparta.trello.domain.user.dto.SignupRequest;
import com.sparta.trello.domain.user.entity.User;
import com.sparta.trello.domain.user.repository.UserRepository;
import com.sparta.trello.domain.user.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private CardRepository cardRepository;

    @InjectMocks
    private UserService userService;


    @Test
    @DisplayName("회원가입 테스트")
    void signupTest() {
        // Given
        SignupRequest signupRequest = new SignupRequest("test@test.com", "password", "username",
            "profile");
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        // When
        userService.signup(signupRequest);

        // Then
        verify(userRepository, times(1)).findByEmail(any());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("프로필 조회 테스트")
    void showProfileTest() {
        // Given
        User user = new User("test@test.com", "password", "username", "profile");
        List<BoardInfo> boardInfoList = new ArrayList<>();
        List<CardInfo> cardInfoList = new ArrayList<>();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(boardRepository.findByUser_Id(any())).thenReturn(boardInfoList);
        when(cardRepository.findByUser_Id(any())).thenReturn(cardInfoList);

        // When
        InfoResponse infoResponse = userService.showProfile(user);

        // Then
        assertEquals(user.getUsername(), infoResponse.getUsername());
        assertEquals(user.getProfile(), infoResponse.getProfile());
        assertEquals(boardInfoList, infoResponse.getBoards());
        assertEquals(cardInfoList, infoResponse.getCards());
    }

    @Test
    @DisplayName("프로필 수정 테스트")
    void updateProfileTest() {
        // Given
        User user = new User("test@test.com", "password", "username", "profile");
        InfoRequest infoRequest = new InfoRequest("newUsername", "newProfile");
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        // When
        userService.updateProfile(user, infoRequest);

        // Then
        assertEquals(infoRequest.getUsername(), user.getUsername());
        assertEquals(infoRequest.getProfile(), user.getProfile());
    }

    @Test
    @DisplayName("중복 회원가입 실패 테스트")
    void signupFailureTest() {
        // Given
        SignupRequest signupRequest = new SignupRequest("test@test.com", "password", "username",
            "profile");
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(new User()));

        // When, Then
        assertThrows(IllegalArgumentException.class, () -> userService.signup(signupRequest));
    }
}
