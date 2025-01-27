"use client";
import ChatListCard from "@/entities/chat/ui/ChatListCard";
import { usePathname } from "next/navigation";

export default function ChatListPage() {
  const pathname = usePathname();
  const CHATLIST = [
    {
      chatId: 123,
      unRead: 100,
      isActive: pathname.includes("/chat/123"),
      participant: {
        name: "라이언",
        profileImage: "https://github.com/shadcn.png",
      },
      lastMessage: "안녕하세요",
      date: "1월 31일",
    },
    {
      chatId: 456,
      unRead: 1,
      isActive: pathname.includes("/chat/456"),
      participant: {
        name: "어피치",
        profileImage: "https://github.com/shadcn.png",
      },
      lastMessage: "잘가요",
      date: "1월 31일",
    },
    {
      chatId: 789,
      unRead: 0,
      isActive: pathname.includes("/chat/789"),
      participant: {
        name: "춘식이",
        profileImage: "https://github.com/shadcn.png",
      },
      lastMessage: "귀여웡",
      date: "1월 31일",
    },
  ];
  return (
    <div>
      {/* 채팅 리스트 아이템 */}
      {CHATLIST.map((chat) => (
        <ChatListCard key={chat.chatId} {...chat} />
      ))}
    </div>
  );
}
