package com.collabit.chat.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageRequestDTO {
    private int roomCode;
    private String message;
    private LocalDateTime timestamp;
}