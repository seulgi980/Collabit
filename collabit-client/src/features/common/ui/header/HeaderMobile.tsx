import HeaderDropDown from "@/entities/common/ui/HeaderDropDown";
import { Button } from "@/shared/ui/button";
import { HeaderProps } from "@/widget/types/Header";
import { AlignJustify } from "lucide-react";
import Image from "next/image";
import Link from "next/link";

const HeaderMobile = ({
  isLogin,
  handleToLogin,
  handleLogout,
  handleToMyPage,
  isChatRoom,
}: HeaderProps) => {
  if (isChatRoom) {
    return null;
  }
  return (
    <header className="flex h-[72px] w-full items-center justify-between px-5 shadow-sm">
      <Link href="/" className="flex h-[24px] w-[65px] items-center">
        <h1 className="sr-only">Collabit</h1>
        <Image
          src="/images/logo-lg.png"
          alt="collabit"
          width={65}
          height={24}
          priority
        />
      </Link>
      {isLogin ? (
        <HeaderDropDown
          Icon={AlignJustify}
          handleToMyPage={handleToMyPage}
          handleLogout={handleLogout}
        />
      ) : (
        <Button variant="outline" onClick={handleToLogin}>
          로그인
        </Button>
      )}
    </header>
  );
};

export default HeaderMobile;
