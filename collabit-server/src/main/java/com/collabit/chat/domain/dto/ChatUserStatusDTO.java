package com.collabit.chat.domain.dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class ChatUserStatusDTO {
    private String userCode;
    private boolean isOnline;
    private Integer roomCode;
}

