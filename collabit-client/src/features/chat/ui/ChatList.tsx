"use client";
import ChatListCard, {
  ChatListCardProps,
} from "@/entities/chat/ui/ChatListCard";
import { cn } from "@/shared/lib/shadcn/utils";
import Link from "next/link";
import { usePathname } from "next/navigation";

export default function ChatList({
  chatList,
}: {
  chatList: ChatListCardProps[];
}) {
  const pathname = usePathname();
  return (
    <div className="flex flex-col items-center gap-3 px-2 md:py-4">
      <div className="flex w-full justify-evenly py-3">
        <Link
          href="/chat"
          className={cn(
            "text-lg font-bold",
            pathname.includes("/chat") && "border-b-2 border-violet-500",
          )}
        >
          일반
        </Link>
        <Link
          href="/feedback"
          className={cn(
            "text-lg font-bold",
            pathname.includes("/feedback") && "border-b-2 border-violet-500",
          )}
        >
          프로젝트
        </Link>
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
