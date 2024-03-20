package com.sparta.trello.domain.comment.repository;

import org.hibernate.annotations.Comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comments, Long> {

}
