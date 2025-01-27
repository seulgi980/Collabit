package com.collabit.chat.repository;

import com.collabit.chat.domain.entity.ChatMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ChatMessageRepositoryTest {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    private ChatMessage message1;
    private ChatMessage message2;
    private ChatMessage message3;

    private final int roomCode = 123;

    @BeforeEach
    void setUp() {
        // 테스트용 메시지 데이터 준비
        message1 = new ChatMessage();
        message1.setRoomCode(roomCode);
        message1.setUserCode("user1");
        message1.setMessage("Hello!");
        message1.setTimestamp(LocalDateTime.now().minusMinutes(2));
        message1.setRead(false);
        chatMessageRepository.save(message1);

        message2 = new ChatMessage();
        message2.setRoomCode(roomCode);
        message2.setUserCode("user2");
        message2.setMessage("How are you?");
        message2.setTimestamp(LocalDateTime.now().minusMinutes(1));
        message2.setRead(false);
        chatMessageRepository.save(message2);

        message3 = new ChatMessage();
        message3.setRoomCode(roomCode);
        message3.setUserCode("user1");
        message3.setMessage("I'm fine!");
        message3.setTimestamp(LocalDateTime.now());
        message3.setRead(true);
        chatMessageRepository.save(message3);
    }

    @Test
    void testFindByRoomCodeOrderByTimestampDesc() {
        Pageable pageable = PageRequest.of(0, 10);
        var page = chatMessageRepository.findByRoomCodeOrderByTimestampDesc(roomCode, pageable);

        assertNotNull(page);
        assertEquals(3, page.getTotalElements());
        List<ChatMessage> messages = page.getContent();
        assertEquals(message3.getMessage(), messages.get(0).getMessage());
        assertEquals(message2.getMessage(), messages.get(1).getMessage());
        assertEquals(message1.getMessage(), messages.get(2).getMessage());
    }

    @Test
    void testFindTopByRoomCodeOrderByTimestampDesc() {
        // 가장 최신 메시지 조회
        ChatMessage latestMessage = chatMessageRepository.findTopByRoomCodeOrderByTimestampDesc(roomCode);

        assertNotNull(latestMessage);
        assertEquals(message3.getMessage(), latestMessage.getMessage());
    }
}
