package com.collabit.chat.repository;

import com.collabit.chat.domain.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    //채팅방 메시지 목록 최신순
    Page<ChatMessage> findByRoomCodeOrderByTimestampDesc(int roomCode, Pageable pageable);

    //채팅방별 최신 메시지
    ChatMessage findTopByRoomCodeOrderByTimestampDesc(int roomCode);

    // 특정 채팅방의 읽지 않은 메시지 개수 카운트
    int countByRoomCodeAndIsReadFalse(int roomCode);

    // 특정 사용자의 읽지 않은 메시지 개수 카운트 (전체 메시지에서)
    int countByUserCodeAndIsReadFalse(String userCode);

    //특정 채팅방의 모든 메시지 읽음 처리
    @Query("{ 'roomCode': ?0, 'userCode': { $ne: ?1 }, 'isRead': false }")
    @Modifying
    void markMessagesAsRead(int roomCode, String userCode);
}
