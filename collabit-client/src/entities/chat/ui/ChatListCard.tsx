import { cn } from "@/shared/lib/shadcn/utils";
import { Avatar, AvatarFallback, AvatarImage } from "@/shared/ui/avatar";
import Link from "next/link";

interface ChatListCardProps {
  unRead: number;
  isActive: boolean;
  chatId: number;
  participant: {
    name: string;
    profileImage: string;
  };
  lastMessage: string;
  date: string;
}

const ChatListCard = ({
  unRead,
  isActive,
  chatId,
  participant,
  lastMessage,
  date,
}: ChatListCardProps) => {
  return (
    <Link
      href={`/chat/${chatId}`}
      className={cn(
        "flex justify-between p-2 hover:bg-gray-100",
        isActive && "bg-violet-100",
      )}
    >
      <div className="flex gap-2">
        <Avatar className="h-10 w-10">
          <AvatarImage src={participant.profileImage} />
          <AvatarFallback>{participant.name.slice(0, 2)}</AvatarFallback>
        </Avatar>
        <div className="flex flex-col justify-between">
          <span className="font-bold">{participant.name}</span>
          <span className="text-sm text-gray-400">{lastMessage}</span>
        </div>
      </div>
      <div className="flex flex-col items-center justify-between">
        <span className="text-xs text-gray-400">{date}</span>
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
