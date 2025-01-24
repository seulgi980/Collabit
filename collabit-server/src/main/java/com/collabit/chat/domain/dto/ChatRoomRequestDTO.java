package com.collabit.chat.domain.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomRequestDTO {
    private String nickname;
    private String message;
}
