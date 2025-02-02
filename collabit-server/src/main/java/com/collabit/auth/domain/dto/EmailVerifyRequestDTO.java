package com.collabit.auth.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmailVerifyRequestDTO {
    private String email;
    private String code;
}
