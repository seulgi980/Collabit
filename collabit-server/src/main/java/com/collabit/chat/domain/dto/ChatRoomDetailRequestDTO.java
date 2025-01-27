package com.collabit.chat.domain.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDetailRequestDTO {
    private int roomCode;
    private int pageNumber;
}
