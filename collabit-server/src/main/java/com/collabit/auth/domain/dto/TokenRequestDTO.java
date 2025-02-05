package com.collabit.auth.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class TokenRequestDTO {

    private String accessToken;
    private String refreshToken;

}
