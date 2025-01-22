package com.collabit.user.repository;

import com.collabit.user.domain.entity.User;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // GitHub 아이디로 사용자 찾기
    Optional<User> findByGithubId(String githubId);

    // email 아이디로 사용자 찾기
    Optional<User> findByEmail(String email);

    // email 아이디 중복 검사
    boolean existsByEmail(String email);

    // nickname 중복검사
    boolean existsByNickname(String nickName);

}
