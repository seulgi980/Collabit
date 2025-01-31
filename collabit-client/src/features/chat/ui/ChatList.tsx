"use client";
import ChatListCard, {
  ChatListCardProps,
} from "@/entities/chat/ui/ChatListCard";
import TabNavButton from "@/entities/common/ui/TabNavButton";

export default function ChatList({
  chatList,
}: {
  chatList: ChatListCardProps[];
}) {
  return (
    <div className="flex flex-col items-center gap-3 px-2 md:py-4">
      <div className="flex w-full justify-evenly py-3">
        <TabNavButton href="/chat">일반</TabNavButton>
        <TabNavButton href="/feedback">프로젝트</TabNavButton>
      </div>
      {/* 채팅 리스트 아이템 */}
      <div className="flex h-[calc(100vh-220px)] w-full flex-col gap-2 overflow-y-auto md:h-[calc(100vh-192px)]">
        {chatList.map((chat) => (
          <ChatListCard key={chat.id} {...chat} />
        ))}
      </div>
    </div>
  );
}
