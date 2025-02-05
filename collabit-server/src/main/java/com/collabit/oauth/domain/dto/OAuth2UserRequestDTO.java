package com.collabit.oauth.domain.dto;

import lombok.*;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Builder
@Getter
@NoArgsConstructor
@ToString
public class OAuth2UserRequestDTO {

    private String githubId;      // Github의 사용자 ID
    private String nickname;      // Github의 username(login)
    private String profileImage;  // Github의 프로필 이미지 URL

    public OAuth2UserRequestDTO(String githubId, String nickname, String profileImage) {
        this.githubId = githubId;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public static OAuth2UserRequestDTO from(OAuth2User oauth2User) {
        return OAuth2UserRequestDTO.builder()
                .githubId(oauth2User.getAttribute("login"))
                .nickname(oauth2User.getAttribute("login"))
                .profileImage(oauth2User.getAttribute("avatar_url"))
                .build();
    }
}
