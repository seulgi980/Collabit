package com.collabit.chat.service;

import com.collabit.chat.domain.dto.ChatMessageRequestDTO;
import com.collabit.chat.domain.dto.ChatMessageResponseDTO;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomDetailService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRedisService chatRedisService;

    // 채팅방 디테일 조회
    public PageResponseDTO getChatRoomDetail(String userCode, ChatRoomDetailRequestDTO requestDTO) {
        int roomCode = requestDTO.getRoomCode();

        // 채팅방 참여 여부 확인
        if (!isUserInChatRoom(userCode, roomCode)) {
            log.debug("User {} is not in chat room", userCode);
            throw new UserNotInChatRoomException();
        }

        Pageable pageable = PageRequest.of(requestDTO.getPageNumber(), 50);
        Page<ChatMessage> chatMessagePage = chatMessageRepository.findByRoomCodeOrderByTimestampDesc(roomCode, pageable);
        log.debug("ChatMessagePage {}", chatMessagePage.toString());


        List<ChatMessageResponseDTO> chatMessages = chatMessagePage.getContent().stream()
                .map(this::convertToResponseDTO)
                .toList();
        log.debug("ChatMessages {}", chatMessages);

        // 채팅방 상세 정보 생성
        ChatRoomDetailResponseDTO chatRoomDetail = buildChatRoomDetailResponse(userCode, roomCode, chatMessages);
        log.debug("ChatRoomDetail {}", chatRoomDetail);

        return PageResponseDTO.builder()
                .content(chatRoomDetail)
                .pageNumber(requestDTO.getPageNumber())
                .pageSize(50)
                .totalElements((int) chatMessagePage.getTotalElements())
                .totalPages(chatMessagePage.getTotalPages())
                .last(chatMessagePage.isLast())
                .hasNext(chatMessagePage.hasNext())
                .build();
    }

    // 특정 채팅방 전체 메시지 읽음 처리 (Redis 연동)
    public void markMessagesAsRead(int roomCode, String userCode) {
        chatRedisService.updateRoomMessageStatus(roomCode, userCode, true);
    }

    // 메시지 저장
    public void saveMessage(ChatMessageRequestDTO chatMessageRequestDTO, String userCode, int roomCode) {
        ChatMessage chatMessage = ChatMessage.builder()
                .roomCode(roomCode)
                .userCode(userCode)
                .message(chatMessageRequestDTO.getMessage())
                .timestamp(LocalDateTime.now())
                .isRead(false)
                .build();

        log.debug("ChatMessage saving... {}", chatMessage);
        chatMessageRepository.save(chatMessage);
        log.info("메시지 저장 완료: Room {}, Message {}", roomCode, chatMessageRequestDTO.getMessage());
    }

    // 채팅방 상세 정보 생성
    private ChatRoomDetailResponseDTO buildChatRoomDetailResponse(String userCode, int roomCode, List<ChatMessageResponseDTO> messages) {
        User user = getUserByUserCode(userCode, roomCode);
        log.debug("ChatRoomDetailResponse {}", user.toString());
        return ChatRoomDetailResponseDTO.builder()
                .messages(messages)
                .roomCode(roomCode)
                .nickname(user.getNickname())
                .profileImage(user.getProfileImage())
                .build();
    }

    // 유저가 채팅방에 있는지 확인
    private boolean isUserInChatRoom(String userCode, int roomCode) {
        return chatRoomRepository.findById(roomCode)
                .map(chatRoom -> chatRoom.getUser1().getCode().equals(userCode) || chatRoom.getUser2().getCode().equals(userCode))
                .orElse(false);
    }

    // 메시지를 Response DTO로 변환
    private ChatMessageResponseDTO convertToResponseDTO(ChatMessage chatMessage) {
        return ChatMessageResponseDTO.builder()
                .roomCode(chatMessage.getRoomCode())
                .nickname(getUserByUserCode(chatMessage.getUserCode(), chatMessage.getRoomCode()).getNickname())
                .message(chatMessage.getMessage())
                .timestamp(chatMessage.getTimestamp())
                .build();
    }

    // 채팅방에서 상대방 정보 가져오기
    private User getUserByUserCode(String userCode, int roomCode) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomCode)
                .orElseThrow(ChatRoomNotFoundException::new);
        return chatRoom.getUser1().getCode().equals(userCode) ? chatRoom.getUser2() : chatRoom.getUser1();
    }
}
