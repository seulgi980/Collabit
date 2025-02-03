package com.collabit.auth.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class UserLoginRequestDTO {

    @NotBlank
    @Email
    private String email;


    @NotBlank
    private String password;

}
