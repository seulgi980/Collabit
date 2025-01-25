package com.collabit.oauth.service;

import com.collabit.oauth.domain.dto.OAuth2UserRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final OAuth2Service oAuth2Service;

    // 실제 access token을 사용해 GitHub API와 통신하고 사용자 정보를 가져와 DTO에 매핑하는 메소드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        // OAuth2UserRequest : Access Token, Client 정보 포함
        // OAuth2User : GitHub에서 받은 사용자 정보 가짐
        OAuth2User oauth2User = super.loadUser(oAuth2UserRequest);

        // GitHub에서 받은 사용자 정보를 OAuth2UserRequestDTO에 매핑
        OAuth2UserRequestDTO oauth2UserRequestDTO = OAuth2UserRequestDTO.builder()
                .githubId(oauth2User.getAttribute("id").toString())
                .nickname(oauth2User.getAttribute("login"))
                .profileImage(oauth2User.getAttribute("avatar_url"))
                .build();

        oAuth2Service.processOAuth2User(oauth2UserRequestDTO);

        // Spring Security의 인증 객체로, SecurityContext에 저장하여 TokenProvider에서 JWT 토큰 생성할 때 사용
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), // 사용자 권한 설정
                oauth2User.getAttributes(), // GitHub에서 받아온 사용자 정보
                "id" // 사용자 구분 Key
        );
    }
}