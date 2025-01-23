"use client";
import { Avatar, AvatarFallback, AvatarImage } from "@/shared/ui/avatar";
import HeaderDropDown from "../headerDropDown/HeaderDropDown";
import { ChevronDown } from "lucide-react";

interface UserAvatarProps {
  user: { name: string };
  handleLogout?: () => void;
  handleToMyPage?: () => void;
}
const UserAvatar = ({
  user,
  handleLogout,
  handleToMyPage,
}: UserAvatarProps) => {
  return (
    <div className="flex items-center gap-2">
      <Avatar>
        <AvatarImage src="https://github.com/shadcn.png" />
        <AvatarFallback>CN</AvatarFallback>
      </Avatar>
      <span className="font-semibold">{user.name}</span>
      {handleToMyPage && handleLogout && (
        <HeaderDropDown
          Icon={ChevronDown}
          handleToMyPage={handleToMyPage}
          handleLogout={handleLogout}
        />
      )}
    </div>
  );
};

export default UserAvatar;
