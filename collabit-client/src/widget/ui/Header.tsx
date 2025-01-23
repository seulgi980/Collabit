"use client";
import NavMobile from "@/entities/ui/navMobile/NavMobile";
import HeaderDesktop from "@/features/ui/header/HeaderDesktop";
import HeaderMobile from "@/features/ui/header/HeaderMobile";
import MENU_LIST from "@/shared/constant/MENU_LIST";
import { QUERY_SIZE } from "@/shared/constant/QUERY_SIZE";
import useMediaQuery from "@/shared/hooks/useMediaQuery";
import { useRouter } from "next/navigation";

const Header = () => {
  const isMobile = useMediaQuery(QUERY_SIZE.md);
  const router = useRouter();
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
          <NavMobile menuList={MENU_LIST} />
        </>
      ) : (
        <HeaderDesktop
          isLogin={false}
          handleToLogin={handleToLogin}
          handleLogout={handleLogout}
          handleToMyPage={handleToMyPage}
          menuList={MENU_LIST}
        />
      )}
    </>
  );
};
export default Header;
