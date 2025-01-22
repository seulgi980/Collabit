package com.collabit.community.repository;

import com.collabit.community.domain.entity.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    Comment findByCode(int parentCommentCode);

    List<Comment> findByPostCode(int postCode);
}
