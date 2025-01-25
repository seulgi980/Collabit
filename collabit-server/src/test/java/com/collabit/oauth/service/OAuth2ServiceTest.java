package com.collabit.oauth.service;

import com.collabit.global.security.SecurityUtil;
import com.collabit.oauth.domain.dto.OAuth2UserRequestDTO;
import com.collabit.oauth.domain.enums.OAuth2Status;
import com.collabit.user.domain.entity.Role;
import com.collabit.user.domain.entity.User;
import com.collabit.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
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
    @DisplayName("기존 GitHub 사용자 로그인 성공")
    void loginSuccessTest() {
        String githubId = "123456";
        OAuth2UserRequestDTO requestDTO = createGitHubUserDTO(githubId);
        User existingUser = createMockUser(githubId);

        when(userRepository.findByGithubId(githubId))
                .thenReturn(Optional.of(existingUser));

        User result = oauth2Service.saveOrLoginOAuth2User(requestDTO);

        verify(userRepository).findByGithubId(githubId);
        verify(userRepository, never()).save(any(User.class));
        assertThat(oauth2Service.getOAuth2Status()).isEqualTo(OAuth2Status.LOGIN_SUCCESS);
        assertThat(result.getGithubId()).isEqualTo(githubId);
    }

    @Test
    @DisplayName("신규 GitHub 사용자 회원가입 성공")
    void signupSuccessTest() {
        String githubId = "123456";
        OAuth2UserRequestDTO requestDTO = createGitHubUserDTO(githubId);
        User newUser = createMockUser(githubId);

        when(userRepository.findByGithubId(githubId))
                .thenReturn(Optional.empty());
        when(userRepository.save(any(User.class)))
                .thenReturn(newUser);

        User result = oauth2Service.saveOrLoginOAuth2User(requestDTO);

        verify(userRepository).findByGithubId(githubId);
        verify(userRepository).save(any(User.class));
        assertThat(oauth2Service.getOAuth2Status()).isEqualTo(OAuth2Status.SIGNUP_SUCCESS);
        assertThat(result).isNotNull()
                .satisfies(user -> {
                    assertThat(user.getGithubId()).isEqualTo(githubId);
                    assertThat(user.getRole()).isEqualTo(Role.ROLE_USER);
                });
    }

    @Test
    @DisplayName("일반 회원의 GitHub 계정 연동 성공")
    void linkGithubSuccessTest() {
        String userCode = "user123";
        String githubId = "git123";
        OAuth2UserRequestDTO githubDTO = createGitHubUserDTO(githubId);
        User normalUser = createMockUserWithoutGithub(userCode);

        when(userRepository.findByCode(userCode)).thenReturn(Optional.of(normalUser));
        when(userRepository.existsByGithubId(githubId)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(normalUser);

        oauth2Service.linkGithubAccount(userCode, githubDTO);

        verify(userRepository).findByCode(userCode);
        verify(userRepository).existsByGithubId(githubId);
        verify(userRepository).save(any(User.class));
        assertThat(oauth2Service.getOAuth2Status()).isEqualTo(OAuth2Status.GITHUB_LINK_SUCCESS);
    }

    @Test
    @DisplayName("존재하지 않는 회원의 GitHub 연동 실패")
    void linkGithubFailWithNonExistentUserTest() {
        String nonExistentCode = "invalid";
        OAuth2UserRequestDTO githubDTO = createGitHubUserDTO("git123");

        when(userRepository.findByCode(nonExistentCode)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> oauth2Service.linkGithubAccount(nonExistentCode, githubDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("이미 GitHub 계정이 연동된 회원의 재연동 실패")
    void linkGithubFailWithAlreadyLinkedUserTest() {
        String userCode = "user123";
        OAuth2UserRequestDTO githubDTO = createGitHubUserDTO("newGit");
        User linkedUser = createMockUser("existingGit");

        when(userRepository.findByCode(userCode)).thenReturn(Optional.of(linkedUser));

        assertThatThrownBy(() -> oauth2Service.linkGithubAccount(userCode, githubDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("이미 GitHub 계정이 연동되어 있습니다.");
    }

    @Test
    @DisplayName("다른 회원이 사용 중인 GitHub 계정 연동 실패")
    void linkGithubFailWithDuplicateGithubIdTest() {
        String userCode = "user123";
        String duplicateGithubId = "duplicate";
        OAuth2UserRequestDTO githubDTO = createGitHubUserDTO(duplicateGithubId);
        User normalUser = createMockUserWithoutGithub(userCode);

        when(userRepository.findByCode(userCode)).thenReturn(Optional.of(normalUser));
        when(userRepository.existsByGithubId(duplicateGithubId)).thenReturn(true);

        assertThatThrownBy(() -> oauth2Service.linkGithubAccount(userCode, githubDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("이미 다른 계정에 연동된 GitHub 계정입니다.");
    }

    @Test
    @DisplayName("processOAuth2User 로그인된 사용자의 GitHub 계정 연동")
    void processOAuth2UserWithExistingUserTest() {
        String userCode = "user123";
        OAuth2UserRequestDTO githubDTO = createGitHubUserDTO("git123");
        User normalUser = createMockUserWithoutGithub(userCode);

        try (MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class)) {
            mockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(userCode);
            when(userRepository.findByCode(userCode)).thenReturn(Optional.of(normalUser));
            when(userRepository.existsByGithubId(githubDTO.getGithubId())).thenReturn(false);
            when(userRepository.save(any(User.class))).thenReturn(normalUser);

            oauth2Service.processOAuth2User(githubDTO);

            verify(userRepository).findByCode(userCode);
            assertThat(oauth2Service.getOAuth2Status()).isEqualTo(OAuth2Status.GITHUB_LINK_SUCCESS);
        }
    }

    @Test
    @DisplayName("processOAuth2User 비로그인 사용자의 GitHub 로그인")
    void processOAuth2UserWithNewUserTest() {
        OAuth2UserRequestDTO githubDTO = createGitHubUserDTO("git123");
        User newUser = createMockUser(githubDTO.getGithubId());

        try (MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class)) {
            mockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(null);
            when(userRepository.findByGithubId(githubDTO.getGithubId())).thenReturn(Optional.empty());
            when(userRepository.save(any(User.class))).thenReturn(newUser);

            oauth2Service.processOAuth2User(githubDTO);

            verify(userRepository).save(any(User.class));
            assertThat(oauth2Service.getOAuth2Status()).isEqualTo(OAuth2Status.SIGNUP_SUCCESS);
        }
    }

    @Test
    @DisplayName("GitHub ID 조회 성공")
    void getUserGithubIdSuccessTest() {
        String userCode = "user123";
        String githubId = "git123";
        User user = createMockUser(githubId);

        try (MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class)) {
            mockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(userCode);
            when(userRepository.findByCode(userCode)).thenReturn(Optional.of(user));

            Object result = oauth2Service.getUserGithubId();

            assertThat(result).isEqualTo(githubId);
            verify(userRepository).findByCode(userCode);
        }
    }

    @Test
    @DisplayName("닉네임 조회 성공")
    void getUserNicknameSuccessTest() {
        String userCode = "user123";
        String nickname = "testUser";
        User user = createMockUser("git123");

        try (MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class)) {
            mockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(userCode);
            when(userRepository.findByCode(userCode)).thenReturn(Optional.of(user));

            Object result = oauth2Service.getUserNickname();

            assertThat(result).isEqualTo(nickname);
            verify(userRepository).findByCode(userCode);
        }
    }

    private OAuth2UserRequestDTO createGitHubUserDTO(String githubId) {
        return OAuth2UserRequestDTO.builder()
                .githubId(githubId)
                .nickname("testUser")
                .profileImage("https://example.com/image.jpg")
                .build();
    }

    private User createMockUser(String githubId) {
        return User.builder()
                .code(UUID.randomUUID().toString())
                .githubId(githubId)
                .nickname("testUser")
                .profileImage("https://example.com/image.jpg")
                .role(Role.ROLE_USER)
                .build();
    }

    private User createMockUserWithoutGithub(String userCode) {
        return User.builder()
                .code(userCode)
                .email("test@test.com")
                .nickname("testUser")
                .profileImage("https://example.com/image.jpg")
                .role(Role.ROLE_USER)
                .build();
    }
}