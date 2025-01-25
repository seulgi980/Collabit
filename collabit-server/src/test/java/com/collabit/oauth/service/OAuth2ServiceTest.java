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
        // given: GitHub ID를 가진 기존 사용자와 요청 DTO 준비
        String githubId = "123456";
        OAuth2UserRequestDTO requestDTO = createGitHubUserDTO(githubId);
        User existingUser = createMockUser(githubId);

        when(userRepository.findByGithubId(githubId))
                .thenReturn(Optional.of(existingUser));

        // when: saveOrLoginOAuth2User 메소드 호출
        User result = oauth2Service.saveOrLoginOAuth2User(requestDTO);

        // then: 로그인 성공 상태 확인 및 GitHub ID 일치 검증
        verify(userRepository).findByGithubId(githubId);
        verify(userRepository, never()).save(any(User.class));
        assertThat(oauth2Service.getOAuth2Status()).isEqualTo(OAuth2Status.LOGIN_SUCCESS);
        assertThat(result.getGithubId()).isEqualTo(githubId);
    }

    @Test
    @DisplayName("신규 GitHub 사용자 회원가입 성공")
    void signupSuccessTest() {
        // given: 새로운 GitHub ID와 요청 DTO 준비
        String githubId = "123456";
        OAuth2UserRequestDTO requestDTO = createGitHubUserDTO(githubId);
        User newUser = createMockUser(githubId);

        when(userRepository.findByGithubId(githubId))
                .thenReturn(Optional.empty());
        when(userRepository.save(any(User.class)))
                .thenReturn(newUser);

        // when: saveOrLoginOAuth2User 메소드로 회원가입 시도
        User result = oauth2Service.saveOrLoginOAuth2User(requestDTO);

        // then: 회원가입 성공 상태와 사용자 정보 검증
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
        // given: GitHub 계정이 없는 일반 회원 준비
        String userCode = "user123";
        String githubId = "git123";
        OAuth2UserRequestDTO githubDTO = createGitHubUserDTO(githubId);
        User normalUser = createMockUserWithoutGithub(userCode);

        when(userRepository.findByCode(userCode)).thenReturn(Optional.of(normalUser));
        when(userRepository.existsByGithubId(githubId)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(normalUser);

        // when: GitHub 계정 연동 시도
        oauth2Service.linkGithubAccount(userCode, githubDTO);

        // then: 연동 성공 상태와 저장 여부 검증
        verify(userRepository).findByCode(userCode);
        verify(userRepository).existsByGithubId(githubId);
        verify(userRepository).save(any(User.class));
        assertThat(oauth2Service.getOAuth2Status()).isEqualTo(OAuth2Status.GITHUB_LINK_SUCCESS);
    }

    @Test
    @DisplayName("존재하지 않는 회원의 GitHub 연동 실패")
    void linkGithubFailWithNonExistentUserTest() {
        // given: 존재하지 않는 사용자 코드 설정
        String nonExistentCode = "invalid";
        OAuth2UserRequestDTO githubDTO = createGitHubUserDTO("git123");

        when(userRepository.findByCode(nonExistentCode)).thenReturn(Optional.empty());

        // when & then: 사용자를 찾을 수 없다는 예외 발생 검증
        assertThatThrownBy(() -> oauth2Service.linkGithubAccount(nonExistentCode, githubDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("이미 GitHub 계정이 연동된 회원의 재연동 실패")
    void linkGithubFailWithAlreadyLinkedUserTest() {
        // given: GitHub 계정이 이미 연동된 사용자 준비
        String userCode = "user123";
        OAuth2UserRequestDTO githubDTO = createGitHubUserDTO("newGit");
        User linkedUser = createMockUser("existingGit");

        when(userRepository.findByCode(userCode)).thenReturn(Optional.of(linkedUser));

        // when & then: 이미 연동된 계정이라는 예외 발생 검증
        assertThatThrownBy(() -> oauth2Service.linkGithubAccount(userCode, githubDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("이 계정은 이미 GitHub와 연동되어 있습니다.");
    }

    @Test
    @DisplayName("다른 회원이 사용 중인 GitHub 계정 연동 실패")
    void linkGithubFailWithDuplicateGithubIdTest() {
        // given: GitHub 계정이 없는 일반 회원과 중복된 GitHub ID 준비
        String userCode = "user123";
        String duplicateGithubId = "duplicate";
        OAuth2UserRequestDTO githubDTO = createGitHubUserDTO(duplicateGithubId);
        User normalUser = createMockUserWithoutGithub(userCode);

        when(userRepository.findByCode(userCode)).thenReturn(Optional.of(normalUser));
        when(userRepository.existsByGithubId(duplicateGithubId)).thenReturn(true);

        // when & then: 중복된 GitHub ID라는 예외 발생 검증
        assertThatThrownBy(() -> oauth2Service.linkGithubAccount(userCode, githubDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("이 GitHub 계정은 이미 다른 계정과 연동되어 있습니다. 다른 GitHub 계정을 사용해 주세요.");
    }

    @Test
    @DisplayName("processOAuth2User 검증: 로그인된 사용자의 GitHub 계정 연동")
    void processOAuth2UserWithExistingUserTest() {
        // given: 로그인된 일반 사용자와 GitHub 연동 정보 준비
        String userCode = "user123";
        OAuth2UserRequestDTO githubDTO = createGitHubUserDTO("git123");
        User normalUser = createMockUserWithoutGithub(userCode);

        try (MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class)) {
            // SecurityUtil이 예외를 던지지 않고 userCode를 반환하도록 설정
            mockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(userCode);
            when(userRepository.findByCode(userCode)).thenReturn(Optional.of(normalUser));
            when(userRepository.existsByGithubId(githubDTO.getGithubId())).thenReturn(false);
            when(userRepository.save(any(User.class))).thenReturn(normalUser);

            // when: GitHub 계정 연동 프로세스 실행
            oauth2Service.processOAuth2User(githubDTO);

            // then: 연동 성공 상태 검증
            verify(userRepository).findByCode(userCode);
            assertThat(oauth2Service.getOAuth2Status()).isEqualTo(OAuth2Status.GITHUB_LINK_SUCCESS);
        }
    }

    @Test
    @DisplayName("processOAuth2User 검증: 비로그인 사용자의 GitHub 로그인")
    void processOAuth2UserWithNewUserTest() {
        // given: 새로운 GitHub 사용자 정보 준비
        OAuth2UserRequestDTO githubDTO = createGitHubUserDTO("git123");
        User newUser = createMockUser(githubDTO.getGithubId());

        try (MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class)) {
            // SecurityUtil이 예외를 던지도록 설정
            mockedStatic.when(SecurityUtil::getCurrentUserId)
                    .thenThrow(new RuntimeException("인증 정보가 없습니다."));
            when(userRepository.findByGithubId(githubDTO.getGithubId())).thenReturn(Optional.empty());
            when(userRepository.save(any(User.class))).thenReturn(newUser);

            // when: GitHub 로그인 프로세스 실행
            oauth2Service.processOAuth2User(githubDTO);

            // then: 회원가입 성공 상태 검증
            verify(userRepository).save(any(User.class));
            assertThat(oauth2Service.getOAuth2Status()).isEqualTo(OAuth2Status.SIGNUP_SUCCESS);
        }
    }

    @Test
    @DisplayName("GitHub ID 조회 성공")
    void getUserGithubIdSuccessTest() {
        // given: GitHub ID가 연동된 사용자 준비
        String userCode = "user123";
        String githubId = "git123";
        User user = createMockUser(githubId);

        try (MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class)) {
            mockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(userCode);
            when(userRepository.findByCode(userCode)).thenReturn(Optional.of(user));

            // when: GitHub ID 조회
            Object result = oauth2Service.getUserGithubId();

            // then: 조회된 GitHub ID 검증
            assertThat(result).isEqualTo(githubId);
            verify(userRepository).findByCode(userCode);
        }
    }

    @Test
    @DisplayName("닉네임 조회 성공")
    void getUserNicknameSuccessTest() {
        // given: 닉네임이 설정된 사용자 준비
        String userCode = "user123";
        String nickname = "testUser";
        User user = createMockUser("git123");

        try (MockedStatic<SecurityUtil> mockedStatic = mockStatic(SecurityUtil.class)) {
            mockedStatic.when(SecurityUtil::getCurrentUserId).thenReturn(userCode);
            when(userRepository.findByCode(userCode)).thenReturn(Optional.of(user));

            // when: 닉네임 조회
            Object result = oauth2Service.getUserNickname();

            // then: 조회된 닉네임 검증
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