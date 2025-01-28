package com.collabit.auth.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class TokenRequestDto {

    private String accessToken;
    private String refreshToken;

}
