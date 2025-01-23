package com.collabit.user.service;

import com.collabit.user.domain.dto.OAuth2UserRequestDTO;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final OAuth2Service oAuth2Service;

    public CustomOAuth2UserService(OAuth2Service oAuth2Service) {
        this.oAuth2Service = oAuth2Service;
    }

    // 실제 access token을 사용해 GitHub API와 통신하고 사용자 정보를 가져와 DTO mapping
    // OAuth2UserRequest : Access Token, Client 정보 포함
    // OAuth2User : GitHub에서 받은 사용자 정보 가짐
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        // Github에서 받은 사용자 정보를 OAuth2UserRequestDTO로 변환
        OAuth2UserRequestDTO oauth2UserRequestDTO = OAuth2UserRequestDTO.builder()
                .githubId(oauth2User.getAttribute("id").toString())
                .nickname(oauth2User.getAttribute("login"))
                .profileImage(oauth2User.getAttribute("avatar_url"))
                .build();

        // 깃허브 연동 요청인 경우 (oAuth2Service에 임시 저장해둔 usercode가 있음)
        String storedUserCode = oAuth2Service.getStoredUserCode();
        if (storedUserCode != null) {
            oAuth2Service.linkGithubAccount(storedUserCode, oauth2UserRequestDTO);
        }

        // OAuth2 로그인/회원가입인 경우
        else {
            oAuth2Service.saveOrLoginOAuth2User(oauth2UserRequestDTO);
        }

        // 사용자 정보와 권한을 담은 DefaultOAuth2User 반환
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), // 사용자 권한 설정
                oauth2User.getAttributes(), // 깃허브에서 받아온 사용자 정보
                "id" // 사용자 구분 Key
        );
    }
}