package com.collabit.chat.service;

import com.collabit.chat.domain.dto.*;
import com.collabit.chat.domain.entity.ChatMessage;
import com.collabit.chat.domain.entity.ChatRoom;
import com.collabit.chat.repository.ChatMessageRepository;
import com.collabit.chat.repository.ChatRoomRepository;
import com.collabit.global.common.PageResponseDTO;
import com.collabit.user.domain.entity.User;
import com.collabit.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomListService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    // 닉네임으로 사용자 찾기
    private User getUserByNickname(String nickname) {
//        return userRepository.findByNickname(nickname)
//                .orElseThrow(() -> new UserNotFoundException());
        return new User();
    }

    // 닉네임으로 채팅방 조회
    public ChatRoomResponseDTO getChatRoomByNickname(String userCode, String nickname) {
        User user = getUserByNickname(nickname);
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
        User user1 = userRepository.findById(userCode1).orElseThrow();
        User user2 = userRepository.findById(userCode2).orElseThrow();

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

    //채팅방 리스트 조회
    public PageResponseDTO getChatRoomList(String userCode, ChatRoomListRequestDTO requestDTO) {
        int size = 15;
        int page = requestDTO.getPageNumber();
        Pageable pageable = PageRequest.of(page, size);
        User user = userRepository.findById(userCode).orElseThrow();
        Page<ChatRoom> chatRoomPage = chatRoomRepository.findByUser1OrUser2(user, user, pageable);

        // 채팅방 리스트 응답 DTO 변환
        List<ChatRoomListResponseDTO> chatRoomList = chatRoomPage.getContent().stream()
                .map(chatRoom -> {
                    // 마지막 메시지 및 마지막 메시지 시간 조회
                    ChatMessage lastMessage = chatMessageRepository.findTopByRoomCodeOrderByTimestampDesc(chatRoom.getCode());
                    int unreadCount = getUnreadMessageCountForRoom(chatRoom.getCode());
                    // 상대 유저 조회
                    User otherUser = chatRoom.getUser1().getCode().equals(userCode) ? chatRoom.getUser2() : chatRoom.getUser1();
                    // 채팅방 리스트 DTO 생성
                    return ChatRoomListResponseDTO.builder()
                            .roomCode(chatRoom.getCode())
                            .lastMessage(lastMessage.getMessage())
                            .lastMessageTime(lastMessage.getTimestamp())
                            .unreadMessageCount(unreadCount)
                            .nickname(otherUser.getNickname())
                            .profileImage(otherUser.getProfileImage())
                            .build();
                })
                .collect(Collectors.toList());

        // PageResponseDTO 생성 및 반환
        return PageResponseDTO.builder()
                .content(chatRoomList)
                .pageNumber(page)
                .pageSize(size)
                .totalElements((int) chatRoomPage.getTotalElements())
                .totalPages(chatRoomPage.getTotalPages())
                .last(chatRoomPage.isLast())
                .hasNext(chatRoomPage.hasNext())
                .build();
    }

    // 모든 채팅방에서 읽지 않은 메시지 개수 반환
    public ChatUnreadResponseDTO getUnreadMessagesForUser(String userCode) {
        return ChatUnreadResponseDTO.builder()
                .isExist(chatMessageRepository.countByUserCodeAndIsReadFalse(userCode) != 0)
                .build();
    }

    // 특정 채팅방에서 읽지 않은 메시지 개수 반환
    public int getUnreadMessageCountForRoom(int roomCode) {
        return chatMessageRepository.countByRoomCodeAndIsReadFalse(roomCode);
    }
}
