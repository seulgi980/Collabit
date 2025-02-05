"use client";
import ChatListCard from "@/entities/chat/ui/ChatListCard";
import ChatNav from "@/entities/chat/ui/ChatNav";
import { useChatList } from "../context/ChatListProvider";
import EmptyChatList from "@/entities/chat/ui/EmptyChatList";
import { useEffect } from "react";
import { useAuth } from "@/features/auth/api/useAuth";
import { useQuery } from "@tanstack/react-query";
import { getChatListAPI } from "@/shared/api/chat";

export default function ChatList() {
  const { userInfo } = useAuth();
  const { chatList, setChatList } = useChatList();
  const { data } = useQuery({
    queryKey: ["chatList", userInfo?.nickname],
    queryFn: getChatListAPI,
    enabled: !!userInfo?.nickname,
  });

  useEffect(() => {
    if (data) {
      setChatList(data);
    }
  }, [data, setChatList]);
  return (
    <div className="flex flex-col items-center gap-3 px-2 md:py-4">
      <ChatNav />

      <div className="flex h-[calc(100vh-220px)] w-full flex-col gap-2 overflow-y-auto md:h-[calc(100vh-192px)]">
        {chatList.length > 0 ? (
          chatList.map((item) => (
            <ChatListCard
              type="chat"
              key={item.roomCode}
              id={item.roomCode}
              nickname={item.nickname}
              profileImage={item.profileImage}
              title={item.nickname}
              description={item.lastMessage}
              updatedAt={item.lastMessageTime}
              unRead={item.unReadMessageCount}
            />
          ))
        ) : (
          <EmptyChatList />
        )}
      </div>
    </div>
  );
}
