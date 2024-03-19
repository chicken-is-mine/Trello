package com.sparta.trello.domain.comment.service;

import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.card.repository.CardRepository;
import com.sparta.trello.domain.comment.dto.CommentRequest;
import com.sparta.trello.domain.comment.entity.Comment;
import com.sparta.trello.domain.comment.repository.CommentRepository;
import com.sparta.trello.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    CommentRepository commentRepository;
    CardRepository cardRepository;
    public void createComment(Long id, CommentRequest request, User user) {
        Card card = cardRepository.findById(id) .orElseThrow(() -> new NullPointerException("존재 하지 않는 카드입니다"));

        Comment comment = new Comment(request,card,user);
        commentRepository.save(comment);

    }
}
