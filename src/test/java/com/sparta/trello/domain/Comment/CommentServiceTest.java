package com.sparta.trello.domain.Comment;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.sparta.trello.domain.board.entity.Board;
import com.sparta.trello.domain.card.entity.Card;
import com.sparta.trello.domain.card.repository.CardRepository;
import com.sparta.trello.domain.comment.dto.CommentRequest;
import com.sparta.trello.domain.comment.entity.Comment;
import com.sparta.trello.domain.comment.repository.CommentRepository;
import com.sparta.trello.domain.comment.service.CommentService;
import com.sparta.trello.domain.user.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    CommentRepository commentRepository;
    @Mock
    CardRepository cardRepository;

    CommentService commentService;

    @BeforeEach
    void setUp() {
        commentService = new CommentService(commentRepository, cardRepository);
    }

    @Test
    @DisplayName("댓글 수정 성공 테스트")
    void updateCommentTest_SUCCESS() {
        //given
        Long Id = 100L;
        Board board = new Board();
        board.setBoardId(Id);
        User user1 = new User();
        user1.setId(Id);
        CommentRequest comment1 = new CommentRequest("새 댓글");
        CommentRequest comment2 = new CommentRequest("수정된 댓글");
        Card card = new Card();
        card.setCardId(Id);

        Comment comment = new Comment(comment1, card, user1);
        given(commentRepository.findById(Id)).willReturn(Optional.of(comment));

        //when
        commentService.updateComment(Id,Id,comment2,user1);
        //then
        assertEquals(comment2.getCommentContent(),comment.getContent());
    }
    @Test
    @DisplayName("댓글 수정 실패 테스트")
    void updateCommentTest_FAIL() {
        //given
        Long Id = 100L;
        Board board = new Board();
        board.setBoardId(Id);
        User user1 = new User();
        user1.setId(Id);
        User user2 = new User();
        user2.setId(200l);
        CommentRequest comment1 = new CommentRequest("새 댓글");
        CommentRequest comment2 = new CommentRequest("수정된 댓글");
        Card card = new Card();
        card.setCardId(Id);

        Comment comment = new Comment(comment1, card, user1);

        given(commentRepository.findById(Id)).willReturn(Optional.of(comment));

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            commentService.updateComment(Id, Id, comment2, user2);
        });
        //then

        assertEquals("댓글 작성자가 아닙니다. 댓글 수정 권한이 없습니다.",exception.getMessage());


    }


}
