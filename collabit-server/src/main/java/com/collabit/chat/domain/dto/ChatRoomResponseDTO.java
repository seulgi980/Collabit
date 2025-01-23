package com.collabit.chat.domain.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomResponseDTO {
    private int roomCode;
    private String nickname;
    private String profileImg;
}
