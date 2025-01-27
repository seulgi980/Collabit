"use client";
import NavMobile from "@/entities/ui/navMobile/NavMobile";
import HeaderDesktop from "@/features/ui/header/HeaderDesktop";
import HeaderMobile from "@/features/ui/header/HeaderMobile";
import MENU_LIST from "@/shared/constant/MENU_LIST";
import { QUERY_SIZE } from "@/shared/constant/QUERY_SIZE";
import { toast } from "@/shared/hooks/use-toast";
import useMediaQuery from "@/shared/hooks/useMediaQuery";
import { usePathname, useRouter } from "next/navigation";
import { useEffect, useState } from "react";

const Header = () => {
  const isMobile = useMediaQuery(QUERY_SIZE.md);
  const pathname = usePathname();
  const router = useRouter();
  const [hasNewChat] = useState(true);
  console.log(pathname);

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
  const handleLogout = () => {
    alert("로그아웃");
  };
  const handleToMyPage = () => {
    router.push("/mypage");
  };

  return (
    <>
      {isMobile ? (
        <>
          <HeaderMobile
            isLogin={false}
            handleToLogin={handleToLogin}
            handleLogout={handleLogout}
            handleToMyPage={handleToMyPage}
          />
          <NavMobile menuList={MENU_LIST} hasNewChat={hasNewChat} />
        </>
      ) : (
        <HeaderDesktop
          isLogin={false}
          handleToLogin={handleToLogin}
          handleLogout={handleLogout}
          handleToMyPage={handleToMyPage}
          menuList={MENU_LIST}
          hasNewChat={hasNewChat}
        />
      )}
    </>
  );
};
export default Header;
