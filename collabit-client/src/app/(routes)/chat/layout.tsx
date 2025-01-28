"use client";

import { usePathname } from "next/navigation";

const ChatLayout = ({
  list,
  room,
}: {
  list: React.ReactNode;
  room: React.ReactNode;
}) => {
  const pathname = usePathname();
  const isChatRoom = pathname.includes("/chat/") && pathname !== "/chat";

  return (
    <>
      {/* 모바일 레이아웃 */}
      <div className="md:hidden">{isChatRoom ? room : list}</div>

      {/* 데스크톱 레이아웃 */}
      <div className="hidden w-full md:flex">
        <div className="w-1/4 min-w-[280px]">{list}</div>
        <div className="w-3/4 bg-gray-100">{room}</div>
      </div>
    </>
  );
};

export default ChatLayout;
