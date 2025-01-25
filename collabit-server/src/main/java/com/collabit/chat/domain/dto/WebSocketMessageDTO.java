package com.collabit.chat.domain.dto;

import com.collabit.chat.domain.entity.ChatMessage;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMessageDTO {
    private String messageType;       // 메시지 타입 (ENTER, MESSAGE, EXIT)
    private int roomCode;
    private String nickname;
    private String content;
    private LocalDateTime timestamp;
}
