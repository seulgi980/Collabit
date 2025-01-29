package com.collabit.chat.domain.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDetailResponseDTO {
    int roomCode;
    String nickname;
    String profileImage;
    List<ChatMessageResponseDTO> messages;
}
