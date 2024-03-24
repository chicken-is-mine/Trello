package com.sparta.trello.domain.user.service;

import com.sparta.trello.domain.board.dto.BoardInfo;
import com.sparta.trello.domain.board.repository.BoardRepository;
import com.sparta.trello.domain.card.dto.CardInfo;
import com.sparta.trello.domain.card.repository.CardRepository;
import com.sparta.trello.domain.user.dto.InfoRequest;
import com.sparta.trello.domain.user.dto.InfoResponse;
import com.sparta.trello.domain.user.dto.SignupRequest;
import com.sparta.trello.domain.user.entity.User;
import com.sparta.trello.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BoardRepository boardRepository;
    private final CardRepository cardRepository;

    public void signup(SignupRequest request) {
        String email = request.getEmail();
        String password = passwordEncoder.encode(request.getPassword());
        String username = request.getUsername();
        String profile = request.getProfile();

        validUser(email);

        User user = new User(email, password, username, profile);
        userRepository.save(user);

    }

    public InfoResponse showProfile(User user) {
        User findUser = userRepository.findById(user.getId())
            .orElseThrow(() -> new NullPointerException("존재 하지 않는 유저입니다."));

        List<BoardInfo> boardInfoList = boardRepository.findByUser_Id(user.getId());
        List<CardInfo> cardInfoList = cardRepository.findByUser_Id(user.getId());

        return new InfoResponse(findUser.getUsername(), findUser.getProfile(), boardInfoList,
            cardInfoList);
    }

    @Transactional
    public void updateProfile(User user, InfoRequest request) {
        User updateUser = userRepository.findById(user.getId())
            .orElseThrow(() -> new NullPointerException("존재 하지 않는 유저입니다."));

        updateUser.updateUser(request.getUsername(), request.getProfile());

    }

    private void validUser(String email) {
        Optional<User> checkUsername = userRepository.findByEmail(email);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }
    }
}
