import { cn } from "@/shared/lib/shadcn/utils";
import {
  ClipboardList,
  Home,
  Layers,
  MessageSquare,
  Users,
} from "lucide-react";
import Link from "next/link";
import { usePathname } from "next/navigation";

const NavMobile = ({
  menuList,
}: {
  menuList: { name: string; href: string }[];
}) => {
  const pathname = usePathname();
  const menus = [{ name: "홈", href: "/" }, ...menuList];
  const icons = {
    홈: Home,
    리포트: ClipboardList,
    프로젝트: Layers,
    커뮤니티: Users,
    채팅: MessageSquare,
  };
  return (
    <nav className="fixed bottom-0 left-0 right-0 z-50 h-20 w-full rounded-3xl bg-white shadow-[0_-4px_6px_-1px_rgba(0,0,0,0.1)]">
      <ul className="flex h-full items-center justify-between px-5">
        {menus.map((i) => {
          const Icon = icons[i.name as keyof typeof icons];
          return (
            <li
              key={i.name}
              className={cn(
                "mx-1 flex h-[56px] items-center justify-center",
                pathname.split("/")[1] === i.href.split("/")[1] &&
                  "border-b-2 border-violet-700 text-violet-700",
              )}
            >
              <Link
                className="flex flex-col items-center justify-center gap-1"
                href={i.href}
              >
                <Icon />
                <span className="text-xs font-medium">{i.name}</span>
              </Link>
            </li>
          );
        })}
      </ul>
    </nav>
  );
};

export default NavMobile;
