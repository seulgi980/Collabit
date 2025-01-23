package com.collabit.user.service;

import com.collabit.user.domain.dto.OAuth2UserRequestDTO;
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
}