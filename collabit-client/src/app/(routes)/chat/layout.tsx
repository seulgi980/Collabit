"use client";

import { useAuth } from "@/features/auth/api/useAuth";
import { ChatListProvider } from "@/features/chat/context/ChatListProvider";
import { usePathname, useRouter } from "next/navigation";
import { useEffect } from "react";
import { useChatRoomList } from "@/features/chat/api/useChatRoomList";
import { useSocket } from "@/features/chat/api/useSocket";

const ChatLayout = ({
  list,
  room,
}: {
  list: React.ReactNode;
  room: React.ReactNode;
}) => {
  const { isAuthenticated, isLoading } = useAuth();
  const router = useRouter();
  const pathname = usePathname();
  const isChatRoom =
    (pathname.includes("/chat/") && pathname !== "/chat") ||
    (pathname.includes("/survey/") && pathname !== "/survey");

  useEffect(() => {
    if (!isLoading && !isAuthenticated) {
      router.push("/login");
    }
  }, [isAuthenticated, isLoading, router]);

  //웹소켓 연결
  useSocket();

  //리스트 렌더링
  const { chatList, hasNextPage, fetchNextPage } = useChatRoomList();

  //디테일 렌더링
  

  return (
    <ChatListProvider
      initialData={chatList}
      hasNextPage={hasNextPage}
      fetchNextPage={fetchNextPage}
    >
      {/* 모바일 레이아웃 */}
      <div className="md:hidden">{isChatRoom ? room : list}</div>

      {/* 데스크톱 레이아웃 */}
      <div className="hidden w-full border-b border-gray-200 md:flex">
        <div className="w-1/4 min-w-[280px] border-r">{list}</div>
        <div className="w-3/4">{room}</div>
      </div>
    </ChatListProvider>
  );
};

export default ChatLayout;
