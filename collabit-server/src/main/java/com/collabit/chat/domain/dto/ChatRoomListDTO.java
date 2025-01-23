package com.collabit.chat.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomListDTO {
    private String roomCode;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private int unreadMessageCount;
    private String nickname;
    private String profileImg;
}
