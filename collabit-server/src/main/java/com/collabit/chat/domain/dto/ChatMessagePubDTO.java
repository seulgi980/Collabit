package com.collabit.chat.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessagePubDTO {
    private int roomCode;
    private String nickname;
    private String message;
    private LocalDateTime timestamp;
}