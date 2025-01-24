package com.collabit.auth.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginRequestDto {
    @Schema(description = "사용자의 이메일", example = "user@example.com", required = true)
    @NotBlank
    @Email
    private final String email;

    @Schema(description = "사용자의 비밀번호 영문, 숫자, 특수문자를 포함하여 8~16자", example = "password1234", required = true)
    @NotBlank
    private final String password;

}
