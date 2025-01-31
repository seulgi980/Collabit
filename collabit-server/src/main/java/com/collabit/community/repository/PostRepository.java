package com.collabit.community.repository;

import com.collabit.community.domain.entity.Post;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    Optional<Post> findByCode(int postCode);

    void deleteByCode(int postCode);
}
