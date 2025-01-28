package com.collabit.chat.service;

import com.collabit.chat.domain.dto.*;
import com.collabit.chat.domain.entity.ChatMessage;
import com.collabit.chat.domain.entity.ChatRoom;
import com.collabit.chat.repository.ChatMessageRepository;
import com.collabit.chat.repository.ChatRoomRepository;
import com.collabit.global.common.PageResponseDTO;
import com.collabit.user.domain.entity.User;
import com.collabit.user.exception.UserNotFoundException;
import com.collabit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomListService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRedisService chatRedisService;
    private final UserRepository userRepository;

    // 닉네임으로 사용자 찾기
    private User getUserByNickname(String nickname) {
        return userRepository.findByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);
    }

    // 닉네임으로 채팅방 조회
    public ChatRoomResponseDTO getChatRoomByNickname(String userCode, ChatRoomRequestDTO requestDTO) {
        User user = getUserByNickname(requestDTO.getNickname());
        String userCode2 = user.getCode();
        String uniqueCode = ChatRoom.generateChatRoomCode(userCode, userCode2);
        Optional<ChatRoom> chatRoom = chatRoomRepository.findByUniqueCode(uniqueCode);
        int roomCode = chatRoom.map(ChatRoom::getCode).orElse(-1);

        return ChatRoomResponseDTO.builder()
                .roomCode(roomCode)
                .build();
    }

    // 채팅방 생성
    public ChatRoomResponseDTO saveChatRoom(String userCode, ChatRoomRequestDTO requestDTO) {
        String nickname = requestDTO.getNickname();

        User user = getUserByNickname(nickname); // 사용자 조회
        String userCode2 = user.getCode();

        // 채팅방 고유 코드 생성
        String uniqueCode = ChatRoom.generateChatRoomCode(userCode, userCode2);
        // 채팅방 중복 확인 또는 새 채팅방 생성
        ChatRoom chatRoom = chatRoomRepository.findByUniqueCode(uniqueCode)
                .orElseGet(() -> createNewChatRoom(userCode, userCode2, uniqueCode));

        return buildChatRoomResponseDTO(chatRoom);
    }

    // 새 채팅방 생성
    private ChatRoom createNewChatRoom(String userCode1, String userCode2, String uniqueCode) {
        User user1 = userRepository.findById(userCode1).orElseThrow(UserNotFoundException::new);
        User user2 = userRepository.findById(userCode2).orElseThrow(UserNotFoundException::new);

        return chatRoomRepository.save(
                ChatRoom.builder()
                        .user1(user1)
                        .user2(user2)
                        .uniqueCode(uniqueCode)
                        .build()
        );
    }

    // ChatRoomResponseDTO 생성
    private ChatRoomResponseDTO buildChatRoomResponseDTO(ChatRoom chatRoom) {
        return ChatRoomResponseDTO.builder()
                .roomCode(chatRoom.getCode())
                .build();
    }

    // 채팅방 리스트 조회
    public PageResponseDTO getChatRoomList(String userCode, ChatRoomListRequestDTO requestDTO) {
        int size = 15;
        Pageable pageable = PageRequest.of(requestDTO.getPageNumber(), size);
        User user = userRepository.findById(userCode).orElseThrow();

        Page<ChatRoom> chatRoomPage = chatRoomRepository.findByUser1OrUser2(user, user, pageable);
        List<ChatRoomListResponseDTO> chatRoomList = chatRoomPage.getContent().stream()
                .map(chatRoom -> buildChatRoomListResponse(chatRoom, userCode))
                .toList();

        return PageResponseDTO.builder()
                .content(chatRoomList)
                .pageNumber(requestDTO.getPageNumber())
                .pageSize(size)
                .totalElements((int) chatRoomPage.getTotalElements())
                .totalPages(chatRoomPage.getTotalPages())
                .last(chatRoomPage.isLast())
                .hasNext(chatRoomPage.hasNext())
                .build();
    }

    // Redis와 연동하여 채팅방 리스트 응답 DTO 생성
    private ChatRoomListResponseDTO buildChatRoomListResponse(ChatRoom chatRoom, String userCode) {
        ChatMessage lastMessage = chatMessageRepository.findTopByRoomCodeOrderByTimestampDesc(chatRoom.getCode());
        int unreadCount = chatRedisService.getUnreadMessagesCount(chatRoom.getCode(), userCode);
        User otherUser = getOtherUser(chatRoom, userCode);

        return ChatRoomListResponseDTO.builder()
                .roomCode(chatRoom.getCode())
                .lastMessage(lastMessage != null ? lastMessage.getMessage() : null)
                .lastMessageTime(lastMessage != null ? lastMessage.getTimestamp() : null)
                .unreadMessageCount(unreadCount)
                .nickname(otherUser.getNickname())
                .profileImage(otherUser.getProfileImage())
                .build();
    }

    // Redis와 연계하여 읽지 않은 메시지 확인
    public ChatUnreadResponseDTO getUnreadMessagesCount(String userCode) {
        return ChatUnreadResponseDTO.builder()
                .isExist(chatRedisService.getUnreadMessagesForUser(userCode) > 0)
                .build();
    }

    // 상대 유저 정보 가져오기
    private User getOtherUser(ChatRoom chatRoom, String userCode) {
        return chatRoom.getUser1().getCode().equals(userCode) ? chatRoom.getUser2() : chatRoom.getUser1();
    }

}
