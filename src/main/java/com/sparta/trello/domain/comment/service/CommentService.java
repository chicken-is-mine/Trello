package com.sparta.trello.domain.comment.service;

import com.sparta.trello.domain.board.entity.BoardUser;
import com.sparta.trello.domain.board.repository.BoardUserJpaRepository;
import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.card.repository.CardRepository;
import com.sparta.trello.domain.comment.dto.CommentRequest;
import com.sparta.trello.domain.comment.entity.Comment;
import com.sparta.trello.domain.comment.repository.CommentRepository;
import com.sparta.trello.domain.user.entity.User;
import jakarta.transaction.Transactional;
import java.util.NoSuchElementException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;
    private final BoardUserJpaRepository boardUserJpaRepository;

    @Transactional
    public void createComment(Long cardId, Long boardId, CommentRequest request, User user) {
        workspaceUser(user, boardId);

        Card card = cardRepository.findById(cardId)
            .orElseThrow(() -> new NullPointerException("존재 하지 않는 카드입니다"));

        Comment comment = new Comment(request, card, user);
        commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(Long commentId, Long boardId, CommentRequest request, User user) {

        workspaceUser(user, boardId);
        Comment comment = findComment(commentId);

        if (comment.getUser().getId().equals(user.getId())) {
            comment.update(request);
        } else {
            throw new IllegalArgumentException("댓글 작성자가 아닙니다. 댓글 수정 권한이 없습니다.");
        }
    }


    public void deleteComment(Long commentId, Long boardId, User user) {

        workspaceUser(user, boardId);
        Comment comment = findComment(commentId);

        if (comment.getUser().getId().equals(user.getId())) {
            commentRepository.delete(comment);
        } else {
            throw new IllegalArgumentException("댓글 작성자가 아닙니다. 댓글 삭제 권한이 없습니다.");
        }
    }

    public void workspaceUser(User user, Long bordId) {
        Optional<BoardUser> board = boardUserJpaRepository.findById(bordId);
        BoardUser boardUser = board.get();
        if (!boardUser.getUser().getId().equals(user.getId())) {
            throw new NoSuchElementException("워크스페이스 권한이 없는 사용자입니다.");
        }
    }

    public Comment findComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new NullPointerException("존재 하지 않는 댓글입니다"));

        return comment;
    }
}
