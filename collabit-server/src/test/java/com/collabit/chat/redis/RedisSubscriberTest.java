package com.collabit.chat.redis;

import com.collabit.chat.domain.dto.WebSocketMessageDTO;
import com.collabit.chat.websocket.WebSocketSessionManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.Message;

import static org.mockito.Mockito.*;

@SpringBootTest
public class RedisSubscriberTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private WebSocketSessionManager sessionManager;

    @InjectMocks
    private RedisSubscriber redisSubscriber;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testOnMessageBroadcast() throws Exception {
        // Given
        ObjectMapper objectMapper = spy(new ObjectMapper());
        String messageBody = "{\"messageType\": \"BROADCAST\", \"nickname\": \"user1\", \"roomCode\": 123, \"message\": \"Test message\"}";
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(messageBody.getBytes());

        RedisSubscriber redisSubscriber = new RedisSubscriber(objectMapper, sessionManager);

        WebSocketMessageDTO webSocketMessage = new WebSocketMessageDTO();
        webSocketMessage.setMessageType("BROADCAST");
        webSocketMessage.setNickname("user1");
        webSocketMessage.setRoomCode(123);
        webSocketMessage.setMessage("Test message");

        when(objectMapper.readValue(messageBody, WebSocketMessageDTO.class)).thenReturn(webSocketMessage);

        // When
        redisSubscriber.onMessage(message, null);

        // Then
        verify(sessionManager, times(1)).broadcastMessage(123, "{\"messageType\":\"BROADCAST\",\"nickname\":\"user1\",\"roomCode\":123,\"message\":\"Test message\"}");
    }

    @Test
    void testOnMessageInvalidType() throws Exception {
        // Given
        String messageBody = "{\"messageType\": \"INVALID\", \"nickname\": \"user1\", \"roomCode\": 123, \"message\": \"Test message\"}";
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(messageBody.getBytes());

        WebSocketMessageDTO webSocketMessage = new WebSocketMessageDTO();
        webSocketMessage.setMessageType("INVALID");
        webSocketMessage.setNickname("user1");
        webSocketMessage.setRoomCode(123);
        webSocketMessage.setMessage("Test message");

        when(objectMapper.readValue(messageBody, WebSocketMessageDTO.class)).thenReturn(webSocketMessage);

        // When
        redisSubscriber.onMessage(message, null);

        // Then
        verify(sessionManager, never()).broadcastMessage(anyInt(), anyString());
    }
}
