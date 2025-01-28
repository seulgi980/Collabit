package com.collabit.chat.domain.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomListRequestDTO {
    private int pageNumber;
}
