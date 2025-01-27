package com.collabit.chat.domain.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMessageDTO {
    private String messageType; // 메시지 유형: CONNECT, MESSAGE, ROOM_LIST, READ_MESSAGE 등
    private String nickname;
    private int roomCode;
    private String message;
}
