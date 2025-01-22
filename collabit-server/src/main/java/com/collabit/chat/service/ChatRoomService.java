package com.collabit.chat.service;

import com.collabit.chat.domain.dto.ChatRoomRequestDTO;
import com.collabit.chat.domain.dto.ChatRoomResponseDTO;
import com.collabit.chat.domain.entity.ChatRoom;
import com.collabit.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    public ChatRoomResponseDTO saveChatRoom(String userCode1, ChatRoomRequestDTO request) {
        String nickname = request.getNickname();
        //repo에서 닉네임으로 user2 조회
        String userCode2 = "2";
        String uniqueCode = ChatRoom.generateChatRoomCode(userCode1, userCode2);

        int roomCode;
        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByUniqueCode(uniqueCode);
        if (chatRoomOptional.isPresent()) roomCode = chatRoomOptional.get().getCode();
        else {
            ChatRoom chatRoom = chatRoomRepository.save(
                    ChatRoom.builder().user1(userRepository.findById(userCode1))
                            .user2(userRepository.findById(userCode2))
                            .uniqueCode(uniqueCode).build());
            roomCode = chatRoom.getCode();
        }
        return ChatRoomResponseDTO.builder().roomCode(roomCode).build();
    }

}
