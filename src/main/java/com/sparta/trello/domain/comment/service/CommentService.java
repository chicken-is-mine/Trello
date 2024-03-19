package com.sparta.trello.domain.comment.service;

import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.card.repository.CardRepository;
import com.sparta.trello.domain.comment.dto.CommentRequest;
import com.sparta.trello.domain.comment.entity.Comment;
import com.sparta.trello.domain.comment.repository.CommentRepository;
import com.sparta.trello.domain.user.entity.User;
import jakarta.transaction.Transactional;
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

    @Transactional
    public void updateComment(Long cardId, Long commentId, CommentRequest request, User user) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new NullPointerException("존재 하지 않는 카드입니다"));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NullPointerException("존재 하지 않는 댓글입니다"));
        //todo: user 검증
        if(card.getCardId() == comment.getCard().getCardId()){
        comment.update(request);
        }else {
            throw new IllegalArgumentException("선택한 카드는 존재하지 않습니다.");
        }
    }
}
