package com.collabit.chat.domain.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ChatUserStatusDTO {
    private String userCode;
    private boolean isOnline;
    private Integer roomCode;
}

