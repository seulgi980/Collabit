"use client";
import { Avatar, AvatarFallback, AvatarImage } from "@/shared/ui/avatar";
import { Button } from "@/shared/ui/button";
import { ArrowLeftIcon } from "lucide-react";
import { usePathname, useRouter } from "next/navigation";

const ChatHeader = ({ nickname, projectName, profileImage }) => {
  const router = useRouter();
  const pathname = usePathname();
  const handleBack = () => {
    router.push(pathname.startsWith("/survey") ? "/survey" : "/chat");
  };
  return (
    <div className="flex w-full items-center gap-2 border-b border-gray-200 bg-white py-3">
      <Button variant="ghost" className="h-8 w-8" onClick={handleBack}>
        <ArrowLeftIcon className="h-full w-full" />
      </Button>
      <div className="flex items-center gap-2">
        <Avatar className="h-6 w-6 md:h-8 md:w-8">
          <AvatarImage src={profileImage} />
          <AvatarFallback>{nickname}</AvatarFallback>
        </Avatar>

        <div className="flex text-sm font-semibold md:text-base">
          <p>{nickname}</p> - <p>{projectName}</p>
        </div>
      </div>
    </div>
  );
};

export default ChatHeader;
