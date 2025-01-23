package com.collabit.chat.domain.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessagePagingDTO {
    private List<ChatMessagePubDTO> messages;
    private int totalCount;
    private int pageNumber;
    private int pageSize;
}
