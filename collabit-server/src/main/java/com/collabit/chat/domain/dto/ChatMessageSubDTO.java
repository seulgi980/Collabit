package com.collabit.chat.domain.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageSubDTO {
    private int roomCode;
    private String sender;
    private String message;
}