import { ChatMessageRequest } from "../request/chat";

export interface ChatUser {
  nickname: string;
  profileImage: string;
}

export interface ChatMessageResponse extends ChatMessageRequest {
  nickname: string;
}

export interface ChatRoomDetailResponse {
  messages: {
    content: ChatMessageResponse[];
  };
  pageInfo: {
    currentPage: number;
    totalPages: number;
  };
}

export interface ChatListResponse extends ChatUser {
  roomCode: number;
  lastMessage: string;
  lastMessageTime: string;
  unReadMessageCount: number;
}

export type ChatRoomResponse = { roomCode: number };

export type ChatUnreadResponse = { isExist: boolean };
