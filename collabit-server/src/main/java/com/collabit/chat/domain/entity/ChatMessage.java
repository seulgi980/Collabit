package com.collabit.chat.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chatMessages")
public class ChatMessage {
    private int roomCode;
    private String userCode;
    private String message;
    private LocalDateTime timestamp;
}
