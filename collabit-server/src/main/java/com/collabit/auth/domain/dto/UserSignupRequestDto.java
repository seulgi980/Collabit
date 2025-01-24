package com.collabit.auth.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSignupRequestDto {

    @Schema(description = "사용자의 이메일", example = "user@example.com", required = true)
    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private final String email;

    @Schema(description = "사용자의 닉네임", example = "nickname", required = true)
    @NotBlank(message = "닉네임을 입력해주세요")
    @Pattern(regexp = "^.{2,16}$",
    message = "닉네임은 2자 이상 16자 이하로 입력해주세요")
    private final String nickname;

    @Schema(description = "사용자의 비밀번호사용자의 비밀번호 영문, 숫자, 특수문자를 포함하여 8~16자", example = "password1234", required = true)
    @NotBlank(message = "비밀번호를 입력해주세요")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함하여 8~16자여야 합니다")
    private final String password;

    @Override
    public String toString() {
        return "SignUpRequest{" +
                "email='" + email + '\'' +
                ", password='[PROTECTED]'" +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
