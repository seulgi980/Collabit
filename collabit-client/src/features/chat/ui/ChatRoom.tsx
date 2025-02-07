"use client";
import ChatBubble from "@/entities/chat/ui/ChatBubble";
import ChatHeader from "@/entities/chat/ui/ChatHeader";
import ChatInput from "@/entities/chat/ui/ChatInput";
import { useState, useRef } from "react";
import { useChat } from "../api/useChat";
import { useChatStore } from "@/shared/lib/stores/chatStore";
import { useAuth } from "@/features/auth/api/useAuth";
import { WebSocketMessage } from "@/shared/types/model/Chat";
import { ChatMessageResponse } from "@/shared/types/response/chat";

const ChatRoom = () => {
  const [message, setMessage] = useState("");
  const { userInfo } = useAuth();
  const messagesEndRef = useRef<HTMLDivElement | null>(null);

  const {
    chatId,
    chatRoomDetail,
    chatMessages,
    updateChatMessages,
    sendMessage,
  } = useChatStore();
  const { chatRoomLoading, chatRoomError, fetchNextPage, hasNextPage } =
    useChat();

  // 메시지 전송 핸들러
  const handleSendMessage = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (!message.trim()) return;
    if (!userInfo || !sendMessage || !chatRoomDetail) {
      console.log(userInfo, sendMessage, chatRoomDetail);
      console.error("❌ WebSocket이 연결되지 않거나 채팅방 정보가 없습니다.");
      return;
    }

    const newMessage: WebSocketMessage = {
      nickname: userInfo.nickname,
      message: message.trim(),
      roomCode: chatId!,
      timestamp: new Date().toISOString(),
    };

    console.log(newMessage);
    sendMessage(newMessage);

    updateChatMessages((prevMessages: ChatMessageResponse[]) => [
      {
        nickname: newMessage.nickname,
        message: newMessage.message,
        timestamp: newMessage.timestamp,
      },
      ...prevMessages,
    ]);
    setMessage("");
  };

  // 채팅방 정보가 없을 경우 로딩 상태 표시
  if (chatRoomLoading) {
    return (
      <div className="flex h-screen items-center justify-center">
        🔄 채팅방 로딩 중...
      </div>
    );
  }

  if (chatRoomError || !chatRoomDetail) {
    return (
      <div className="flex h-screen items-center justify-center">
        ❌ 채팅방 정보를 불러오지 못했습니다.
      </div>
    );
  }

  return (
    <div className="flex h-screen w-full flex-col gap-3 py-4 md:h-[calc(100vh-108px)] md:px-2">
      <ChatHeader
        nickname={chatRoomDetail.nickname}
        profileImage={chatRoomDetail.profileImage}
      />
      <div
        className="flex w-full flex-1 flex-col-reverse gap-2 overflow-y-auto rounded-lg bg-white px-2 py-3 md:px-4"
        onScroll={(e) => {
          if (e.currentTarget.scrollTop === 0 && hasNextPage) {
            fetchNextPage();
          }
        }}
      >
        {chatMessages?.map((chat, index) => (
          <ChatBubble
            key={index}
            isMe={chat.nickname === userInfo?.nickname}
            message={chat.message}
            date={new Date(chat.timestamp).toLocaleString()}
            userInfo={{
              name: chat.nickname,
              profileImage: chatRoomDetail.profileImage,
            }}
          />
        ))}
        <div ref={messagesEndRef} />
      </div>
      <ChatInput
        message={message}
        setMessage={setMessage}
        handleSendMessage={handleSendMessage}
      />
    </div>
  );
};

export default ChatRoom;
