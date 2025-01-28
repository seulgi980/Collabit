package com.collabit.community.repository;

import com.collabit.community.domain.entity.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Optional<Comment> findByCode(int parentCommentCode);

    List<Comment> findByPostCode(int postCode);
}
