package com.collabit.user.service;

import com.collabit.user.domain.dto.OAuth2UserRequestDTO;
import com.collabit.user.domain.entity.Role;
import com.collabit.user.domain.entity.User;
import com.collabit.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OAuth2Service {
    private final UserRepository userRepository;

    public OAuth2Service(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // GitHub ID로 기존 사용자 찾아서 있으면 로그인, 없으면 회원가입
    @Transactional
    public User saveOrLoginOAuth2User(OAuth2UserRequestDTO oAuth2UserRequestDTO) {
        return userRepository.findByGithubId(oAuth2UserRequestDTO.getGithubId())
                .map(user -> user) // 기존 사용자가 있으면 그대로 반환 (로그인)
                .orElseGet(() -> signUpOAuth2User(oAuth2UserRequestDTO)); // 없으면 새로 저장 (회원가입)
    }

    // 새로운 GitHub 사용자 생성 및 저장
    private User signUpOAuth2User(OAuth2UserRequestDTO userRequestDTO) {
        User newGitUser = User.builder()
                .code(UUID.randomUUID().toString()) // 고유한 사용자 코드 생성
                .githubId(userRequestDTO.getGithubId())
                .nickname(userRequestDTO.getNickname())
                .profileImage(userRequestDTO.getProfileImage())
                .role(Role.ROLE_USER) // 기본 사용자 권한 설정
                .build();

        return userRepository.save(newGitUser);
    }
}
