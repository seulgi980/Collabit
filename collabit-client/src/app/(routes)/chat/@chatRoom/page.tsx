"use client";

import ChatBubble from "@/entities/ui/chat/ChatBubble";
import ChatHeader from "@/entities/ui/chat/ChatHeader";
import ChatInput from "@/entities/ui/chat/ChatInput";
import { useState } from "react";

const ChatRoomPage = () => {
  const [message, setMessage] = useState("");
  const handleSendMessage = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    console.log(message);
    setMessage("");
  };
  return (
    <div className="flex h-screen w-full flex-col gap-3 px-2 py-4">
      {/* 채팅방 헤더 */}
      <ChatHeader />
      {/* 채팅방 메시지 목록 */}
      <div className="flex h-full w-full flex-1 flex-col gap-2 rounded-lg bg-white px-4 py-3">
        <ChatBubble
          isMe={false}
          message="더미"
          date="2025-01-27"
          userInfo={{
            name: "Name",
            profileImage: "https://github.com/shadcn.png",
          }}
        />
        <ChatBubble isMe={true} message="더미" date="2025-01-27" />
      </div>
      {/* 채팅방 메시지 입력창 */}
      <ChatInput
        message={message}
        setMessage={setMessage}
        handleSendMessage={handleSendMessage}
      />
    </div>
  );
};

export default ChatRoomPage;
