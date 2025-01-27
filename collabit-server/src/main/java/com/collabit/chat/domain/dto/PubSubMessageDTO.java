package com.collabit.chat.domain.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PubSubMessageDTO {
    private String messageType;
    private String userCode;
    private int roomCode;
    private Object payload;
}
