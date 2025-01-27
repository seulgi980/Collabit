package com.collabit.user.repository;

import com.collabit.user.domain.entity.Role;
import com.collabit.user.domain.entity.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        // given: 테스트에서 공통으로 사용할 사용자 객체 생성 및 저장
        testUser = User.builder()
                .code(UUID.randomUUID().toString())
                .githubId("testGitUser")
                .email("test@example.com")
                .nickname("testNickname")
                .profileImage("default.png")
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(testUser);
    }

    @Test
    @DisplayName("usercode로 사용자 찾기 테스트")
    void findByCode() {
        // when: 저장된 사용자의 코드로 검색
        Optional<User> foundUser = userRepository.findByCode(testUser.getCode());

        // then: 검색된 사용자 정보 검증
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getCode()).isEqualTo(testUser.getCode());
    }

    @Test
    @DisplayName("존재하지 않는 usercode로 사용자 찾기 테스트")
    void findByCode_NotFound() {
        // given: 존재하지 않는 사용자 코드 설정
        String nonExistentCode = "nonexistent-code";

        // when: 존재하지 않는 코드로 검색
        Optional<User> foundUser = userRepository.findByCode(nonExistentCode);

        // then: 결과가 비어있는지 검증
        assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("GitHub 아이디로 사용자 찾기 테스트")
    void findByGithubId() {
        // when: GitHub 아이디로 사용자 검색
        Optional<User> foundUser = userRepository.findByGithubId(testUser.getGithubId());

        // then: 검색된 사용자의 GitHub 아이디 검증
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getGithubId()).isEqualTo(testUser.getGithubId());
    }

    @Test
    @DisplayName("GitHub 아이디 존재 여부 확인 테스트")
    void existsByGithubId() {
        // given: BeforeEach에서 설정된 테스트 사용자와 존재하지 않는 GitHub 아이디
        String nonExistentGithubId = "nonexistent";

        // when: 존재하는 아이디와 존재하지 않는 아이디로 각각 검색
        boolean exists = userRepository.existsByGithubId(testUser.getGithubId());
        boolean notExists = userRepository.existsByGithubId(nonExistentGithubId);

        // then: 각각의 결과 검증
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("이메일로 사용자 찾기 테스트")
    void findByEmail() {
        // when: 이메일로 사용자 검색
        Optional<User> foundUser = userRepository.findByEmail(testUser.getEmail());

        // then: 검색된 사용자의 이메일 검증
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    @DisplayName("이메일 중복 검사 테스트")
    void existsByEmail() {
        // given: BeforeEach에서 설정된 테스트 사용자와 존재하지 않는 이메일
        String nonExistentEmail = "nonexistent@example.com";

        // when: 존재하는 이메일과 존재하지 않는 이메일로 각각 검색
        boolean exists = userRepository.existsByEmail(testUser.getEmail());
        boolean notExists = userRepository.existsByEmail(nonExistentEmail);

        // then: 각각의 결과 검증
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }

    @Test
    @DisplayName("닉네임 중복 검사 테스트")
    void existsByNickname() {
        // given: BeforeEach에서 설정된 테스트 사용자와 존재하지 않는 닉네임
        String nonExistentNickname = "nonexistentNickname";

        // when: 존재하는 닉네임과 존재하지 않는 닉네임으로 각각 검색
        boolean exists = userRepository.existsByNickname(testUser.getNickname());
        boolean notExists = userRepository.existsByNickname(nonExistentNickname);

        // then: 각각의 결과 검증
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}
