package com.collabit.community.repository;

import com.collabit.community.domain.entity.Image;
import com.collabit.community.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    List<Image> findByPost(Post post);
    void deleteByUrl(String url);
}
