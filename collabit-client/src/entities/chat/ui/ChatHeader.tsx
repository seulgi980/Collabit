"use client";
import { Avatar, AvatarFallback, AvatarImage } from "@/shared/ui/avatar";
import { Button } from "@/shared/ui/button";
import { ArrowLeftIcon } from "lucide-react";
import { useRouter } from "next/navigation";

const ChatHeader = () => {
  const router = useRouter();
  const handleBack = () => {
    router.push("/chat");
  };
  return (
    <div className="flex w-full items-center gap-2 rounded-lg bg-white px-5 py-2">
      <Button variant="ghost" className="h-8 w-8" onClick={handleBack}>
        <ArrowLeftIcon className="h-full w-full" />
      </Button>
      <div className="flex items-center gap-2">
        <Avatar>
          <AvatarImage src="https://github.com/shadcn.png" />
          <AvatarFallback>CN</AvatarFallback>
        </Avatar>
        <div className="flex font-semibold">
          <p>Name</p> - <p>Last message</p>
        </div>
      </div>
    </div>
  );
};

export default ChatHeader;
