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
  const isChatRoom =
    (pathname.includes("/chat/") && pathname !== "/chat") ||
    (pathname.includes("/feedback/") && pathname !== "/feedback");
  return (
    <>
      {/* 모바일 레이아웃 */}
      <div className="md:hidden">{isChatRoom ? room : list}</div>

      {/* 데스크톱 레이아웃 */}
      <div className="hidden w-full border-b border-gray-200 md:flex">
        <div className="w-1/4 min-w-[280px] border-r">{list}</div>
        <div className="w-3/4">{room}</div>
      </div>
    </>
  );
};

export default ChatLayout;
