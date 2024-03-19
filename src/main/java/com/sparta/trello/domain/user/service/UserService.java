package com.sparta.trello.domain.user.service;

import com.sparta.trello.domain.user.dto.SignupRequest;
import com.sparta.trello.domain.user.dto.SignupResponse;
import com.sparta.trello.domain.user.entity.User;
import com.sparta.trello.domain.user.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(SignupRequest request) {
        String email = request.getEmail();
        String password = passwordEncoder.encode(request.getPassword());
        String username = request.getUsername();
        String profile = request.getProfile();

        validUser(email);

        User user = new User(email, password, username, profile);
        userRepository.save(user);

    }

    private void validUser(String email) {
        Optional<User> checkUsername = userRepository.findByEmail(email);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }
    }

}
