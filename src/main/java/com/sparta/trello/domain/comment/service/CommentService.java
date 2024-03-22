package com.sparta.trello.domain.comment.service;

import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.card.repository.CardRepository;
import com.sparta.trello.domain.comment.dto.CommentRequest;
import com.sparta.trello.domain.comment.dto.CommentResponse;
import com.sparta.trello.domain.comment.entity.Comment;
import com.sparta.trello.domain.comment.repository.CommentRepository;
import com.sparta.trello.domain.user.entity.User;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;

    @Transactional
    public void createComment(Long cardId, Long boardId, CommentRequest request, User user) {
        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new NullPointerException("존재 하지 않는 카드입니다"));

        Comment comment = new Comment(request, card, user);
        commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(Long commentId, Long boardId, CommentRequest request, User user) {
        Comment comment = findComment(commentId);

        if (comment.getUser().getId().equals(user.getId())) {
            comment.update(request);
        } else {
            throw new IllegalArgumentException("댓글 작성자가 아닙니다. 댓글 수정 권한이 없습니다.");
        }
    }


    public void deleteComment(Long commentId, Long boardId, User user) {
        Comment comment = findComment(commentId);

        if (comment.getUser().getId().equals(user.getId())) {
            commentRepository.delete(comment);
        } else {
            throw new IllegalArgumentException("댓글 작성자가 아닙니다. 댓글 삭제 권한이 없습니다.");
        }
    }

    public Comment findComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new NullPointerException("존재 하지 않는 댓글입니다"));

        return comment;
    }

    public List<CommentResponse> getComments(Long cardId) {
        findCard(cardId);
        List<Comment> commentList = commentRepository.findByCardCardId(cardId);
        return convertToDtoList(commentList);
    }

    private List<CommentResponse> convertToDtoList(List<Comment> commentList) {
        List<CommentResponse> commentResponseDtoList = new ArrayList<>();
        for (Comment comment : commentList) {
            commentResponseDtoList.add(new CommentResponse(comment));
        }
        return commentResponseDtoList;
    }

    public Card findCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new NullPointerException("없는 카드입니다."));
        return card;
    }
}
