package com.collabit.chat.domain.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chatMessages")
@CompoundIndex(name = "room_code_timestamp_idx", def = "{'roomCode' : 1, 'timestamp' : -1}")
public class ChatMessage {
    @Id
    private String _id;

    private int roomCode;
    private String userCode;
    private String message;
    private LocalDateTime timestamp;
    private boolean isRead;
}
