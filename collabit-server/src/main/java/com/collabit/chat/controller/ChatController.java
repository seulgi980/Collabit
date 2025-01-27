//package com.collabit.chat.controller;
//
//import com.collabit.chat.domain.dto.*;
//import com.collabit.chat.service.ChatRoomDetailService;
//import com.collabit.chat.service.ChatRoomListService;
//import com.collabit.chat.service.ChatService;
//import com.collabit.global.common.PageResponseDTO;
//import com.collabit.global.security.SecurityUtil;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Tag(name = "ChatController", description = "채팅 API")
//@RequiredArgsConstructor
//@RestController
//@RequestMapping("/api/chat")
//public class ChatController {
//
//    private final ChatRoomListService chatRoomListService;
//    private final ChatRoomDetailService chatRoomDetailService;
//
//    @Operation(summary = "채팅방 생성 또는 조회", description = "채팅방을 생성하거나 존재하는 채팅방을 조회합니다.")
//    @PostMapping("/room")
//    public ResponseEntity<?> getOrCreateChatRoom(@RequestBody ChatRoomRequestDTO requestDTO, @RequestHeader("Authorization") String token) {
//        String userCode = "1";
//        ChatRoomResponseDTO responseDTO = chatRoomListService.saveChatRoom(userCode, requestDTO);
//        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
//    }
//
//    @Operation(summary = "채팅방 목록 조회", description = "사용자의 채팅방 목록을 조회합니다.")
//    @PostMapping("/room/list")
//    public ResponseEntity<?> getChatRoomList(@RequestBody ChatRoomListRequestDTO requestDTO, @RequestHeader("Authorization") String token) {
//        String userCode = "1";
//        PageResponseDTO responseDTO = chatRoomListService.getChatRoomList(userCode, requestDTO);
//        if (((List<?>) responseDTO.getContent()).isEmpty()) return ResponseEntity.noContent().build();
//        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
//    }
//
//    @Operation(summary = "채팅방 메시지 조회", description = "채팅방의 메시지를 조회합니다.")
//    @GetMapping("/room/{roomCode}")
//    public ResponseEntity<?> getChatMessages(@RequestBody ChatRoomDetailRequestDTO requestDTO, @RequestHeader("Authorization") String token, @PathVariable int roomCode) {
//        String userCode = "1";
//        PageResponseDTO responseDTO = chatRoomDetailService.getChatRoomDetail(userCode, requestDTO);
//        if (((ChatRoomDetailResponseDTO) responseDTO.getContent()).getMessages().isEmpty())
//            return ResponseEntity.noContent().build();
//        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
//    }
//
//    @Operation(summary = "읽지 않은 메시지 조회", description = "사용자가 읽지 않은 메시지 수를 조회합니다.")
//    @GetMapping("/unread")
//    public ResponseEntity<?> getUnreadMessages(@RequestHeader("Authorization") String token) {
//        String userCode = "1";
//        ChatUnreadResponseDTO responseDTO = chatRoomListService.getUnreadMessagesForUser(userCode);
//        return ResponseEntity.ok(responseDTO);
//    }
//
//    @Operation(summary = "닉네임으로 채팅방 조회", description = "닉네임을 기반으로 채팅방을 조회합니다.")
//    @PostMapping("/room/nickname")
//    public ResponseEntity<?> getChatRoomWithNickname(@RequestHeader("Authorization") String token, ChatRoomRequestDTO requestDTO) {
//        String userCode = "200c31dc-9762-4cf2-b2f5-ed96e8f1318f";
//        ChatRoomResponseDTO responseDTO = chatRoomListService.getChatRoomByNickname(userCode, requestDTO);
//        if (responseDTO.getRoomCode() == 0) return ResponseEntity.noContent().build();
//        return ResponseEntity.ok(responseDTO);
//    }
//}
