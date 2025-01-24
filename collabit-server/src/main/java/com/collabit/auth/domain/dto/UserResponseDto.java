package com.collabit.auth.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private String nickname;
    private String profileImage;
    private boolean isGithub;

}
