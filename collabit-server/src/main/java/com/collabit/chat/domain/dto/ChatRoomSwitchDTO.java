package com.collabit.chat.domain.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomSwitchDTO {
    private int oldRoomCode;
    private int newRoomCode;
    private String userCode;
}