package com.collabit.chat.domain.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomListResponseDTO {
    private int roomCode;
    private String lastMessage;
    private LocalDateTime lastMessageTime;
    private int unreadMessageCount;
    private String nickname;
    private String profileImage;
}
