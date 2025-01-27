package com.collabit.community.repository;

import com.collabit.community.domain.entity.PostLike;
import com.collabit.community.domain.entity.PostLikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, PostLikeId> {
    int countById_PostCode(int postCode);
    boolean existsById_UserCodeAndId_PostCode(String userCode, int postCode);
}
