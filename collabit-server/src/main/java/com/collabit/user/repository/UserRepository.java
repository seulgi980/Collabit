package com.collabit.user.repository;

import com.collabit.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByCode(String userCode);

    // GitHub 아이디로 사용자 찾기
    Optional<User> findByGithubId(String githubId);

    // 해당 GitHub 아이디가 이미 존재하는지 확인
    boolean existsByGithubId(String githubId);
}
