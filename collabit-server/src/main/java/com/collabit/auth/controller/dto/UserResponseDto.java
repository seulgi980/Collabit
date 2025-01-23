package com.collabit.auth.controller.dto;

import com.collabit.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
public class UserResponseDto {

    private String nickname;
    private String profileImage;
    private boolean isGithub;

}
