import { Avatar, AvatarFallback, AvatarImage } from "@/shared/ui/avatar";

interface ChatBubbleProps {
  isMe: boolean;
  message: string;
  date: string;
  userInfo?: {
    name: string;
    profileImage: string;
  };
}
const ChatBubble = ({ isMe, message, userInfo, date }: ChatBubbleProps) => {
  if (isMe) {
    return (
      <div className="flex gap-2 self-end">
        <div className="self-end text-xs text-gray-400">{date}</div>
        <span className="rounded-bl-lg rounded-br-lg rounded-tl-lg bg-violet-100 px-3 py-2 text-xs md:text-sm">
          {message}
        </span>
      </div>
    );
  }
  return (
    <div className="flex flex-col gap-1">
      {userInfo && (
        <div className="flex items-center gap-1">
          <Avatar className="flex h-6 w-6 gap-3">
            <AvatarImage src={userInfo.profileImage} />
            <AvatarFallback>{userInfo.name[0]}</AvatarFallback>
          </Avatar>
          <div className="text-xs font-semibold md:text-sm">
            {userInfo.name}
          </div>
        </div>
      )}
      <div className="flex gap-2">
        <span className="rounded-bl-lg rounded-br-lg rounded-tr-lg bg-gray-100 px-3 py-2 text-xs md:text-sm">
          {message}
        </span>
        <div className="self-end text-nowrap text-xs text-gray-400">{date}</div>
      </div>
    </div>
  );
};

export default ChatBubble;
