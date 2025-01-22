"use client";

import UserAvatar from "@/entities/userAvatar/ui/UserAvatar";
import { cn } from "@/shared/lib/shadcn/utils";
import { Button } from "@/shared/ui/button";
import {
  NavigationMenu,
  NavigationMenuItem,
  NavigationMenuLink,
  NavigationMenuList,
} from "@/shared/ui/navigation-menu";
import Image from "next/image";
import Link from "next/link";
import { usePathname } from "next/navigation";
import { useState } from "react";

const HeaderDesktop = () => {
  const pathname = usePathname();

  const user = {
    name: "춤추는 라이언",
  };
  const menus = [
    { name: "리포트", href: "/report" },
    { name: "프로젝트", href: "/project" },
    {
      name: "커뮤니티",
      href: "/community",
    },
    { name: "채팅", href: "/chat" },
  ];

  const [isLogin, setIsLogin] = useState(true);

  return (
    <header className="fixed left-0 top-0 flex h-[92px] w-full justify-between px-20 py-4 shadow-md">
      {/* 로고 */}
      <Link
        href="/"
        className="flex h-[60px] w-[150px] items-center justify-center p-4"
      >
        <Image
          src="/images/logo-lg.png"
          alt="collabit"
          width={120}
          height={50}
          priority
        />
      </Link>
      <div className="flex items-center gap-4">
        {/* 메뉴 */}
        <NavigationMenu>
          <NavigationMenuList className="flex items-center">
            {menus.map((i) => (
              <NavigationMenuItem
                key={i.name}
                className={cn(
                  "rounded-md px-4 py-2 hover:bg-slate-100",
                  pathname.includes(i.href) && "bg-slate-100",
                )}
              >
                <NavigationMenuLink className="text-sm" href={i.href}>
                  {i.name}
                </NavigationMenuLink>
              </NavigationMenuItem>
            ))}
          </NavigationMenuList>
        </NavigationMenu>
        {isLogin ? (
          <UserAvatar user={user} />
        ) : (
          <Button variant="outline">로그인</Button>
        )}
      </div>
    </header>
  );
};

export default HeaderDesktop;
