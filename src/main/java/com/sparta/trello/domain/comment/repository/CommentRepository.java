package com.sparta.trello.domain.comment.repository;

import com.sparta.trello.domain.comment.entity.Comment;
import com.sparta.trello.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
