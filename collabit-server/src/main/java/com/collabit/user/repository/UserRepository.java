package com.collabit.user.repository;

import com.collabit.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // GitHub 아이디로 사용자 찾기
    Optional<User> findByGithubId(String githubId);

}
