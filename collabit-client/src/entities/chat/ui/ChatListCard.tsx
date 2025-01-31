import { cn } from "@/shared/lib/shadcn/utils";
import { Avatar, AvatarFallback, AvatarImage } from "@/shared/ui/avatar";
import Link from "next/link";
import { usePathname } from "next/navigation";

export interface ChatListCardProps {
  id: number;
  unRead: number;
  participant: {
    name: string;
    profileImage: string;
  };
  description: string;
  date: string;
}

const ChatListCard = ({
  id,
  unRead,
  participant,
  description,
  date,
}: ChatListCardProps) => {
  const pathname = usePathname();
  const isChat = pathname.includes("/chat");
  const isActive = pathname.split("/").pop() === String(id);
  return (
    <Link
      href={`/${isChat ? "chat" : "feedback"}/${id}`}
      className={cn(
        "flex justify-between p-2 hover:bg-gray-100",
        isActive && "bg-violet-100",
      )}
    >
      <div className="flex w-full gap-2">
        <Avatar className="h-10 w-10">
          <AvatarImage src={participant.profileImage} />
          <AvatarFallback>{participant.name.slice(0, 2)}</AvatarFallback>
        </Avatar>
        <div className="flex flex-col justify-between">
          <span className="text-sm font-bold md:text-base">
            {participant.name}
          </span>
          <span className="w-[calc(100vw-160px)] truncate text-xs text-gray-400 md:w-[160px]">
            {description}
          </span>
        </div>
      </div>
      <div className="flex flex-col items-center justify-between">
        <span className="text-nowrap text-xs text-gray-400">{date}</span>
        {unRead > 0 && (
          <span className="flex h-6 w-6 items-center justify-center rounded-full border border-red-700 bg-red-600 text-[10px] text-white shadow-md">
            {unRead >= 100 ? "99+" : unRead}
          </span>
        )}
      </div>
    </Link>
  );
};

export default ChatListCard;
