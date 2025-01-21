package com.collabit.user.repository;

import com.collabit.user.domain.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test; //junit5
import org.junit.jupiter.api.DisplayName; //junit5
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("GitHub 아이디로 사용자 찾기 테스트")
    public void testFindByGithubId() {
        // Given: 테스트용 깃허브 유저 데이터 저장
        User gitUser = User.githubBuilder()
                .githubId("testGitUser")
                .nickname("testNickname")
                .profileImage("default.png")
                .build();

        userRepository.save(gitUser);

        // When: GitHub 아이디로 사용자 검색
        Optional<User> foundUser = userRepository.findByGithubId("testGitUser");

        System.out.println(foundUser.get().getGithubId());

        // Then: 사용자 정보 검증
        Assertions.assertThat(foundUser).isPresent();
        Assertions.assertThat(foundUser.get().getGithubId()).isEqualTo("testGitUser");
        Assertions.assertThat(foundUser.get().getCode()).isNotNull();
        Assertions.assertThat(foundUser.get().getCode()).matches("^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$"); // UUID 형식 검증
    }

    @Test
    @DisplayName("GitHub 아이디로 사용자 찾기 - 존재하지 않을 때")
    public void testFindByGithubId_NotFound() {
        // When: 존재하지 않는 아이디로 검색
        Optional<User> foundUser = userRepository.findByGithubId("noneId");

        // Then: 결과가 비어 있는지 확인
        Assertions.assertThat(foundUser).isEmpty();
    }

}
