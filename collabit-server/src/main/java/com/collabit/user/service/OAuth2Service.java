package com.collabit.user.service;

import com.collabit.user.domain.dto.OAuth2UserRequestDTO;
import com.collabit.user.domain.entity.Role;
import com.collabit.user.domain.entity.User;
import com.collabit.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class OAuth2Service {
    private final UserRepository userRepository;

    private String tempUserCode; // 연동을 위해 유저코드 임시 저장
    @Getter
    private AuthStatus authStatus; // 상태 저장 필드

    public enum AuthStatus {
        GITHUB_LINK_SUCCESS,   // 깃허브 연동 성공
        LOGIN_SUCCESS,         // 로그인 성공
        SIGNUP_SUCCESS         // 회원가입 성공
    }

    public OAuth2Service(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 유저코드 임시 저장
    public void storeUserCodeForLinking(String userCode) {
        this.tempUserCode = userCode;
    }

    // 유저코드 반환 (깃허브 연동일 경우 사용자 정보와 함께 넘겨주기 위함)
    public String getStoredUserCode() {
        return this.tempUserCode;
    }

    public void clearAuthStatus() {
        authStatus = null;
        tempUserCode = null;
    }

    // GitHub ID로 기존 사용자 찾아서 있으면 로그인, 없으면 회원가입
    @Transactional
    public User saveOrLoginOAuth2User(OAuth2UserRequestDTO oAuth2UserRequestDTO) {
        return userRepository.findByGithubId(oAuth2UserRequestDTO.getGithubId())
                // 기존 사용자가 있으면 그대로 반환 (로그인)
                .map(user -> {
                    authStatus = AuthStatus.LOGIN_SUCCESS;
                    return user;
                })
                // 없으면 새로 저장 (회원가입)
                .orElseGet(() -> {
                    authStatus = AuthStatus.SIGNUP_SUCCESS;
                    return signUpOAuth2User(oAuth2UserRequestDTO);
                });
    }

    // 새로운 GitHub 사용자 생성 및 저장
    private User signUpOAuth2User(OAuth2UserRequestDTO oAuth2UserRequestDTO) {
        User newGitUser = User.builder()
                .code(UUID.randomUUID().toString()) // 고유한 사용자 코드 생성
                .githubId(oAuth2UserRequestDTO.getGithubId())
                .nickname(oAuth2UserRequestDTO.getNickname())
                .profileImage(oAuth2UserRequestDTO.getProfileImage())
                .role(Role.ROLE_USER) // 기본 사용자 권한 설정
                .build();

        return userRepository.save(newGitUser);
    }

    // 일반 회원이 깃허브 계정을 연동할 경우 해당 유저 레코드에 깃허브 계정 추가
    @Transactional
    public void linkGithubAccount(String userCode, OAuth2UserRequestDTO githubUser) {
        User user = userRepository.findByCode(userCode)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 이미 GitHub 계정이 연동된 경우
        if (user.getGithubId() != null) {
            throw new GithubAlreadyLinkedException("이미 GitHub 계정이 연동되어 있습니다.");
        }

        // GitHub ID가 이미 다른 계정에 연동된 경우 체크
        if (userRepository.existsByGithubId(githubUser.getGithubId())) {
            throw new GithubAlreadyLinkedException("이미 다른 계정에 연동된 GitHub 계정입니다.");
        }

        user.linkGithub(githubUser.getGithubId());
        userRepository.save(user);
        authStatus = AuthStatus.GITHUB_LINK_SUCCESS;
    }

}
