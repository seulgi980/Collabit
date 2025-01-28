package com.collabit.chat.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMessageDTO {
    @JsonProperty("messageType")
    private String messageType;

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("roomCode")
    private int roomCode;

    @JsonProperty("message")
    private String message;
}