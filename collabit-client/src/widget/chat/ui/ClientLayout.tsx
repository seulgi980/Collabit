"use client";

import { usePathname } from "next/navigation";
import { useState, useEffect } from "react";

const ClientLayout = ({
  list,
  room,
}: {
  list: React.ReactNode;
  room: React.ReactNode;
}) => {
  const pathname = usePathname();
  const [isMounted, setIsMounted] = useState(false);

  useEffect(() => {
    setIsMounted(true);
  }, []);

  const isChatRoom = pathname.includes("/chat/") && pathname !== "/chat";

  // 하이드레이션 이슈 방지
  if (!isMounted) {
    return (
      <div className="hidden h-full w-full md:flex">
        <div className="w-1/4 min-w-[200px] bg-slate-400">{list}</div>
        <div className="w-3/4 bg-gray-100">{room}</div>
      </div>
    );
  }

  return (
    <>
      {/* 모바일 레이아웃 */}
      <div className="md:hidden">{isChatRoom ? room : list}</div>

      {/* 데스크톱 레이아웃 */}
      <div className="hidden h-full w-full md:flex">
        <div className="w-1/4 min-w-[200px] bg-slate-400">{list}</div>
        <div className="w-3/4 bg-gray-100">{room}</div>
      </div>
    </>
  );
};

export default ClientLayout;
