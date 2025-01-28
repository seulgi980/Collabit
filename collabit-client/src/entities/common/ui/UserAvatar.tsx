"use client";
import { Avatar, AvatarFallback, AvatarImage } from "@/shared/ui/avatar";
import HeaderDropDown from "./HeaderDropDown";
import { ChevronDown } from "lucide-react";
import { UserInfo } from "@/shared/types/model/User";

interface UserAvatarProps {
  user: UserInfo;
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
        <AvatarImage src={user.profileImage} />
        <AvatarFallback>{user.nickname.slice(0, 2)}</AvatarFallback>
      </Avatar>
      <span className="font-semibold">{user.nickname}</span>
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
