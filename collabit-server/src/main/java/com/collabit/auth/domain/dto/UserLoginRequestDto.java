package com.collabit.auth.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class UserLoginRequestDto {

    @NotBlank
    @Email
    private final String email;


    @NotBlank
    private final String password;

}
