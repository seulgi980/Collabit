package com.collabit.user.service;

import com.collabit.oauth.domain.dto.OAuth2UserRequestDTO;
import com.collabit.oauth.exception.GithubAlreadyLinkedException;
import com.collabit.oauth.service.OAuth2Service;
import com.collabit.user.domain.entity.Role;
import com.collabit.user.domain.entity.User;
import com.collabit.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OAuth2ServiceTest {

    @InjectMocks
    private OAuth2Service oauth2Service;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("새로운 GitHub 사용자는 회원가입된다")
    void signUpTest() {
        // given: GitHub에서 받은 사용자 mock data
        String githubId = "123456";
        OAuth2UserRequestDTO requestDTO = OAuth2UserRequestDTO.builder()
                .githubId(githubId)
                .nickname("testUser")
                .profileImage("https://example.com/image.jpg")
                .build();

        // DB에 사용자가 없는 상황
        when(userRepository.findByGithubId(githubId))
                .thenReturn(Optional.empty());

        User savedUser = User.builder()
                .code(UUID.randomUUID().toString())
                .githubId(githubId)
                .nickname("testUser")
                .profileImage("https://example.com/image.jpg")
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // when
        User result = oauth2Service.saveOrLoginOAuth2User(requestDTO);

        // then
        // 메소드 호출 검증: 회원가입의 경우 findByGithubId, save가 둘 다 발생
        verify(userRepository).findByGithubId(githubId);
        verify(userRepository).save(any(User.class));

        // 저장된 사용자 정보 검증
        assertThat(result)
            .satisfies(user -> {
                assertThat(user.getGithubId()).isEqualTo(githubId);
                assertThat(user.getNickname()).isEqualTo("testUser");
                assertThat(user.getRole()).isEqualTo(Role.ROLE_USER);
            });
    }

    @Test
    @DisplayName("기존 GitHub 사용자는 로그인된다")
    void loginTest() {
        // given: GitHub에서 받은 사용자 mock data
        String githubId = "123456";
        OAuth2UserRequestDTO requestDTO = OAuth2UserRequestDTO.builder()
                .githubId(githubId)
                .nickname("testUser")
                .profileImage("https://example.com/image.jpg")
                .build();

        // 이미 DB에 저장된 동일한 깃허브 계정의 유저
        User existingUser = User.builder()
                .code(UUID.randomUUID().toString())
                .githubId(githubId)
                .nickname("existingUser")
                .profileImage("https://example.com/old-image.jpg")
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.findByGithubId(githubId))
                .thenReturn(Optional.of(existingUser));

        // when
        oauth2Service.saveOrLoginOAuth2User(requestDTO);

        // then
        // 메소드 호출 검증: 로그인의 경우 save는 실행되면 안됨
        verify(userRepository).findByGithubId(githubId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("일반 회원이 GitHub 계정을 연동할 수 있다")
    void linkGithubSuccessTest() {
        // given: 일반 회원 정보와 GitHub 계정 정보
        String userCode = "user123"; // JWT 토큰이 없으므로 controller usercode 수정해서 진행
        String githubId = "git123";

        User normalUser = User.builder()
                .code(userCode)
                .email("user@test.com")
                .nickname("normalUser")
                .profileImage("default.jpg")
                .role(Role.ROLE_USER)
                .build();

        OAuth2UserRequestDTO githubUser = OAuth2UserRequestDTO.builder()
                .githubId(githubId)
                .nickname("gitUser")
                .profileImage("github.jpg")
                .build();

        when(userRepository.findByCode(userCode)).thenReturn(Optional.of(normalUser));
        when(userRepository.existsByGithubId(githubId)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(normalUser);

        // when
        oauth2Service.linkGithubAccount(userCode, githubUser);

        // then
        verify(userRepository).findByCode(userCode);
        verify(userRepository).existsByGithubId(githubId);
        verify(userRepository).save(any(User.class));
        assertThat(oauth2Service.getAuthStatus()).isEqualTo(OAuth2Service.AuthStatus.GITHUB_LINK_SUCCESS);
    }

    @Test
    @DisplayName("존재하지 않는 회원은 GitHub 연동에 실패한다")
    void linkGithubFailWhenUserNotFoundTest() {
        // given
        String nonExistentUserCode = "nonexistent";
        OAuth2UserRequestDTO githubUser = OAuth2UserRequestDTO.builder()
                .githubId("git123")
                .nickname("gitUser")
                .profileImage("github.jpg")
                .build();

        when(userRepository.findByCode(nonExistentUserCode)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() ->
                oauth2Service.linkGithubAccount(nonExistentUserCode, githubUser)
        )
                .isInstanceOf(RuntimeException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("이미 GitHub 계정이 연동된 회원은 다시 연동할 수 없다")
    void linkGithubFailWhenAlreadyLinkedTest() {
        // given: 이미 GitHub가 연동된 회원
        String userCode = "user123";
        User alreadyLinkedUser = User.builder()
                .code(userCode)
                .email("user@test.com")
                .githubId("existingGithub")
                .nickname("linkedUser")
                .profileImage("profile.jpg")
                .role(Role.ROLE_USER)
                .build();

        OAuth2UserRequestDTO newGithubUser = OAuth2UserRequestDTO.builder()
                .githubId("newGithub")
                .nickname("gitUser")
                .profileImage("github.jpg")
                .build();

        when(userRepository.findByCode(userCode)).thenReturn(Optional.of(alreadyLinkedUser));

        // when & then
        assertThatThrownBy(() ->
                oauth2Service.linkGithubAccount(userCode, newGithubUser)
        )
                .isInstanceOf(GithubAlreadyLinkedException.class)
                .hasMessage("이미 GitHub 계정이 연동되어 있습니다.");
    }

    @Test
    @DisplayName("다른 회원이 사용 중인 GitHub 계정은 연동할 수 없다")
    void linkGithubFailWhenGithubIdAlreadyInUseTest() {
        // given: 일반 회원과 이미 사용 중인 GitHub ID
        String userCode = "user123";
        String alreadyUsedGithubId = "usedGithub";

        User normalUser = User.builder()
                .code(userCode)
                .email("user@test.com")
                .nickname("normalUser")
                .profileImage("profile.jpg")
                .role(Role.ROLE_USER)
                .build();

        OAuth2UserRequestDTO githubUser = OAuth2UserRequestDTO.builder()
                .githubId(alreadyUsedGithubId)
                .nickname("gitUser")
                .profileImage("github.jpg")
                .build();

        when(userRepository.findByCode(userCode)).thenReturn(Optional.of(normalUser));
        when(userRepository.existsByGithubId(alreadyUsedGithubId)).thenReturn(true);

        // when & then
        assertThatThrownBy(() ->
                oauth2Service.linkGithubAccount(userCode, githubUser)
        )
                .isInstanceOf(GithubAlreadyLinkedException.class)
                .hasMessage("이미 다른 계정에 연동된 GitHub 계정입니다.");
    }
}