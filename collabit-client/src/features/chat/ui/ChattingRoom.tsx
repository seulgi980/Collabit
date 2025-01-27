"use client";

import ChatBubble from "@/entities/chat/ui/ChatBubble";
import ChatHeader from "@/entities/chat/ui/ChatHeader";
import ChatInput from "@/entities/chat/ui/ChatInput";
import { useState } from "react";

const ChattingRoom = ({ chatId }: { chatId: number }) => {
  console.log(chatId, "client");

  const [message, setMessage] = useState("");
  const handleSendMessage = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    console.log(message);
    setMessage("");
  };
  const TEMP_CHAT_LIST = [
    {
      id: 1,
      message: "이건 너",
      date: "오후 12:00",
      isMe: false,
      userInfo: {
        name: "Name",
        profileImage: "https://github.com/shadcn.png",
      },
    },
    {
      id: 2,
      message: "이건 나",
      date: "오후 12:01",
      isMe: true,
      userInfo: undefined,
    },
  ];
  return (
    <div className="flex w-full flex-col gap-3 px-2 py-4">
      <ChatHeader />
      <div className="flex h-full w-full flex-1 flex-col gap-2 rounded-lg bg-white px-4 py-3">
        {TEMP_CHAT_LIST.map((chat) => (
          <ChatBubble
            key={chat.id}
            isMe={chat.isMe}
            message={chat.message}
            date={chat.date}
            userInfo={chat.userInfo}
          />
        ))}
      </div>
      <ChatInput
        message={message}
        setMessage={setMessage}
        handleSendMessage={handleSendMessage}
      />
    </div>
  );
};

export default ChattingRoom;
