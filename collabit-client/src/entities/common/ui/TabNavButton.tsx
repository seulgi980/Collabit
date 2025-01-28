"use client";
import { cn } from "@/shared/lib/shadcn/utils";
import Link from "next/link";
import { usePathname } from "next/navigation";

const TabNavButton = ({
  children,
  href,
}: {
  children: React.ReactNode;
  href: string;
}) => {
  const pathname = usePathname();
  return (
    <Link
      href={href}
      className={cn(
        "text-lg font-bold",
        pathname.includes(href) && "border-b-2 border-violet-500",
      )}
    >
      {children}
    </Link>
  );
};
export default TabNavButton;
