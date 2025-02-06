"use client";
import ChatBubble from "@/entities/chat/ui/ChatBubble";
import ChatHeader from "@/entities/chat/ui/ChatHeader";
import ChatInput from "@/entities/chat/ui/ChatInput";
import { useState, useRef } from "react";
import { useChat } from "../api/useChat";
import { useSocket } from "../api/useSocket";

const ChatRoom = ({ id }: { id: number }) => {
  const [message, setMessage] = useState("");
  const messagesEndRef = useRef<HTMLDivElement | null>(null);

  const {
    chatRoom,
    messages,
    chatRoomLoading,
    chatRoomError,
    fetchNextPage,
    hasNextPage,
  } = useChat();

  // ✅ 메시지 전송 핸들러
  const handleSendMessage = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!message.trim()) return;
    // sendMessage({
    //   roomCode: id,
    //   message,
    //   messageType: "text",
    //   nickname: chatRoom?.nickname || "user",
    // });
    setMessage("");
  };

  // ✅ 채팅방 정보가 없을 경우 로딩 상태 표시
  if (chatRoomLoading) {
    return (
      <div className="flex h-screen items-center justify-center">
        🔄 채팅방 로딩 중...
      </div>
    );
  }

  if (chatRoomError || !chatRoom) {
    return (
      <div className="flex h-screen items-center justify-center">
        ❌ 채팅방 정보를 불러오지 못했습니다.
      </div>
    );
  }

  return (
    <div className="flex h-screen w-full flex-col gap-3 py-4 md:h-[calc(100vh-108px)] md:px-2">
      <ChatHeader
        nickname={chatRoom.nickname}
        profileImage={chatRoom.profileImage}
      />
      <div
        className="flex w-full flex-1 flex-col-reverse gap-2 overflow-y-auto rounded-lg bg-white px-2 py-3 md:px-4"
        onScroll={(e) => {
          if (e.currentTarget.scrollTop === 0 && hasNextPage) {
            fetchNextPage();
          }
        }}
      >
        {messages.map((chat, index) => (
          <ChatBubble
            key={index}
            isMe={chat.nickname === chatRoom.nickname}
            message={chat.message}
            date={new Date(chat.timestamp).toLocaleString()}
            userInfo={{
              name: chat.nickname,
              profileImage: chatRoom.profileImage,
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
