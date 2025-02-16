"use client";
import NavMobile from "@/entities/common/ui/NavMobile";
import { useAuth } from "@/features/auth/api/useAuth";
import HeaderDesktop from "@/features/common/ui/header/HeaderDesktop";
import HeaderMobile from "@/features/common/ui/header/HeaderMobile";
import MENU_LIST from "@/shared/constant/MENU_LIST";
import { QUERY_SIZE } from "@/shared/constant/QUERY_SIZE";
import { toast } from "@/shared/hooks/use-toast";
import useMediaQuery from "@/shared/hooks/useMediaQuery";
import { useNotificationStore } from "@/shared/lib/stores/NotificationStore";
import { useShallow } from "zustand/shallow";

import { usePathname, useRouter } from "next/navigation";
import { useEffect } from "react";

const Header = () => {
  const isMobile = useMediaQuery(QUERY_SIZE.md);
  const pathname = usePathname();
  const router = useRouter();

  const { userInfo, isAuthenticated, logout } = useAuth();
  const isChatRoom =
    (pathname.includes("/chat/") && pathname !== "/chat") ||
    (pathname.includes("/survey/") && pathname !== "/survey");

  const { surveyRequests, surveyResponses, chatRequests } =
    useNotificationStore(
      useShallow((state) => ({
        surveyRequests: state.surveyRequests,
        surveyResponses: state.surveyResponses,
        chatRequests: state.chatRequests,
      })),
    );
  console.log("surveyRequests", surveyRequests);
  console.log("surveyResponses", surveyResponses);
  console.log("chatRequests", chatRequests);

  const hasNewChat = surveyRequests.length > 0 || chatRequests.length > 0;
  const hasNewResponse = surveyResponses.length > 0;
  console.log("hasNewChat", hasNewChat);
  console.log("hasNewResponse", hasNewResponse);

  // 채팅 알림
  useEffect(() => {
    if (hasNewChat && pathname === "/") {
      toast({
        title: "새로운 채팅이 있습니다.",
        description: "채팅 확인해주세요.",
      });
    }
  }, [hasNewChat, pathname]);
  const handleToLogin = () => {
    router.push("/login");
  };
  const handleLogout = async () => {
    await logout();
  };
  const handleToMyPage = () => {
    router.push("/mypage");
  };

  return (
    <>
      {isMobile ? (
        <>
          <HeaderMobile
            isLogin={isAuthenticated ?? false}
            handleToLogin={handleToLogin}
            handleLogout={handleLogout}
            handleToMyPage={handleToMyPage}
            isChatRoom={isChatRoom}
          />
          <NavMobile
            menuList={MENU_LIST}
            hasNewChat={hasNewChat}
            hasNewResponse={hasNewResponse}
            isChatRoom={isChatRoom}
          />
        </>
      ) : (
        <HeaderDesktop
          isLogin={isAuthenticated ?? false}
          handleToLogin={handleToLogin}
          handleLogout={handleLogout}
          handleToMyPage={handleToMyPage}
          userInfo={userInfo}
          menuList={MENU_LIST}
          hasNewChat={hasNewChat}
          hasNewResponse={hasNewResponse}
        />
      )}
    </>
  );
};
export default Header;
