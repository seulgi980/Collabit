package com.collabit.chat.repository;

import com.collabit.chat.domain.entity.ChatRoom;
import com.collabit.user.domain.entity.Role;
import com.collabit.user.domain.entity.User;
import com.collabit.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class ChatRoomRepositoryTest {

    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private ChatRoom chatRoom;

    @BeforeEach
    void setUp() {
        // 테스트용 사용자 데이터 준비
        user1 = User.builder()
                .email("user1@example.com")
                .password("password")
                .nickname("User1")
                .profileImage("profileImage1")
                .role(Role.ROLE_USER)
                .build();

        user2 = User.builder()
                .email("user2@example.com")
                .password("password")
                .nickname("User2")
                .profileImage("profileImage2")
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user1);
        userRepository.save(user2);

        // 채팅방 2개 생성
        chatRoom = new ChatRoom();
        chatRoom.setUniqueCode(ChatRoom.generateChatRoomCode(user1.getEmail(), user2.getEmail()));
        chatRoom.setUser1(user1);
        chatRoom.setUser2(user2);
        chatRoom.setCreatedAt(LocalDateTime.now());
        chatRoomRepository.save(chatRoom);
    }

    @Test
    void testFindByUniqueCode() {
        Optional<ChatRoom> foundRoom = chatRoomRepository.findByUniqueCode(chatRoom.getUniqueCode());
        assertTrue(foundRoom.isPresent());
        assertEquals(chatRoom.getUniqueCode(), foundRoom.get().getUniqueCode());
    }

    @Test
    void testFindByUser1OrUser2() {
        Pageable pageable = PageRequest.of(0, 15);
        var page = chatRoomRepository.findByUser1OrUser2(user1, user1, pageable);

        assertNotNull(page);
        assertEquals(1, page.getTotalElements());
        List<ChatRoom> rooms = page.getContent();
        assertTrue(rooms.contains(chatRoom));
    }
}
