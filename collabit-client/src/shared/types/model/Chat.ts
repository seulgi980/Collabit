export interface ChatRoom {
  code: number;
  userCode1: string;
  userCode2: string;
}

export interface ChatMessage {
  roomCode: number;
  userCode: string;
  message: string;
  timestamp: number;
  isRead: boolean;
}

export interface WebSocketMessage {
  messageType: string;
  roomCode: number;
  nickname: string;
  message: string;
}

export interface ChatUser {
  nickname: string;
  profileImage: string;
}
