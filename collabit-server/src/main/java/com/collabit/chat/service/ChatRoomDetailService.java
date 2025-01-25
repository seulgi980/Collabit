package com.collabit.chat.service;

import com.collabit.chat.domain.dto.ChatMessagePubDTO;
import com.collabit.chat.domain.dto.ChatMessageSubDTO;
import com.collabit.chat.domain.dto.ChatRoomDetailRequestDTO;
import com.collabit.chat.domain.dto.ChatRoomDetailResponseDTO;
import com.collabit.chat.domain.entity.ChatMessage;
import com.collabit.chat.domain.entity.ChatRoom;
import com.collabit.chat.exception.ChatRoomNotFoundException;
import com.collabit.chat.exception.UserNotInChatRoomException;
import com.collabit.chat.repository.ChatMessageRepository;
import com.collabit.chat.repository.ChatRoomRepository;
import com.collabit.global.common.PageResponseDTO;
import com.collabit.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomDetailService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomListService chatRoomListService;

    // 채팅방 디테일 조회
    public PageResponseDTO getChatRoomDetail(String userCode, ChatRoomDetailRequestDTO requestDTO) {
        int roomCode = requestDTO.getRoomCode();
        if (!isUserInChatRoom(userCode, roomCode)) {
            throw new UserNotInChatRoomException();
        }

        int size = 50;
        int page = requestDTO.getPageNumber();
        Pageable pageable = PageRequest.of(page, size);
        Page<ChatMessage> chatMessagePage = chatMessageRepository.findByRoomCodeOrderByTimestampDesc(requestDTO.getRoomCode(), pageable);

        List<ChatMessagePubDTO> chatMessages = chatMessagePage.getContent().stream()
                .map(message -> convertToPubDTO(message, roomCode))
                .toList();

        User user = getUserByUserCode(userCode, roomCode);

        // 채팅방 세부 정보에 메시지와 방 정보 넣기
        ChatRoomDetailResponseDTO chatRoomDetail = ChatRoomDetailResponseDTO.builder()
                .messages(chatMessages)
                .roomCode(roomCode)
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .build();

        return PageResponseDTO.builder()
                .content(chatRoomDetail)
                .pageNumber(page)
                .pageSize(size)
                .totalElements((int) chatMessagePage.getTotalElements())
                .totalPages(chatMessagePage.getTotalPages())
                .last(chatMessagePage.isLast())
                .hasNext(chatMessagePage.hasNext())
                .build();

    }

    // 메시지 저장
    public void saveMessage(ChatMessageSubDTO chatMessageSubDTO, String userCode, int roomCode) {
        ChatMessage chatMessage = ChatMessage.builder()
                .roomCode(roomCode)
                .userCode(userCode)
                .message(chatMessageSubDTO.getMessage())
                .timestamp(LocalDateTime.now())
                .isRead(false)
                .build();

        chatMessageRepository.save(chatMessage);
        log.info("메시지 저장 완료: Room {}, Message {}", roomCode, chatMessageSubDTO.getMessage());
    }

    // 엔티티 -> PubDTO 변환
    public ChatMessagePubDTO convertToPubDTO(ChatMessage chatMessage, int roomCode) {
        String senderNickname = getUserByUserCode(chatMessage.getUserCode(), roomCode).getNickname();

        return ChatMessagePubDTO.builder()
                .roomCode(chatMessage.getRoomCode())
                .nickname(senderNickname)
                .message(chatMessage.getMessage())
                .timestamp(chatMessage.getTimestamp())
                .build();
    }

    // 채팅방에서 상대방 정보 가져오기
    private User getUserByUserCode(String userCode, int roomCode) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomCode)
                .orElseThrow(() -> new ChatRoomNotFoundException());
        if (chatRoom.getUser1().getCode().equals(userCode)) {
            return chatRoom.getUser2();
        } else {
            return chatRoom.getUser1();
        }
    }

    // 유저가 채팅방에 있는지 확인
    public boolean isUserInChatRoom(String userCode, int roomCode) {
        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findById(roomCode);
        if (chatRoomOptional.isPresent()) {
            ChatRoom chatRoom = chatRoomOptional.get();
            return chatRoom.getUser1().getCode().equals(userCode) || chatRoom.getUser2().getCode().equals(userCode);
        }
        return false;
    }

    // 특정 채팅방 전체 메시지 읽음 처리
    public void markMessagesAsRead(int roomCode, String userCode) {
        chatMessageRepository.markMessagesAsRead(roomCode, userCode);
    }

}
