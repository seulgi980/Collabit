package com.collabit.chat.domain.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDetailResponseDTO {
    int roomCode;
    String nickname;
    String profileImage;
    List<ChatMessagePubDTO> messages;
}
