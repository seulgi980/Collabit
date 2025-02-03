package com.collabit.auth.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@AllArgsConstructor
@Builder
@ToString
@NoArgsConstructor
public class CheckNicknameRequestDTO {
    @NotBlank(message = "닉네임을 입력해주세요")
    @Pattern(regexp = "^.{2,16}$",
            message = "닉네임은 2자 이상 16자 이하로 입력해주세요")
    private String nickname;
}
