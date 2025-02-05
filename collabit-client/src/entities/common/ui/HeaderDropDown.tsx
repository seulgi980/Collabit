import { UserInfo } from "@/shared/types/model/User";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/shared/ui/dropdown-menu";
import { LogOutIcon, User } from "lucide-react";
import { ComponentType } from "react";
import UserAvatar from "./UserAvatar";

interface HeaderDropDownProps {
  handleToMyPage: () => void;
  handleLogout: () => void;
  user?: UserInfo;
  Icon: ComponentType;
}

const HeaderDropDown = ({
  Icon,
  user,
  handleToMyPage,
  handleLogout,
}: HeaderDropDownProps) => {
  return (
    <DropdownMenu>
      <DropdownMenuTrigger className="flex items-center justify-center gap-2">
        {user && <UserAvatar user={user} />}
        <Icon />
      </DropdownMenuTrigger>
      <DropdownMenuContent>
        <DropdownMenuLabel>계정</DropdownMenuLabel>
        <DropdownMenuSeparator />
        <DropdownMenuItem onClick={handleToMyPage}>
          <User className="h-4 w-4" />
          마이페이지
        </DropdownMenuItem>

        <DropdownMenuItem onClick={handleLogout}>
          <LogOutIcon className="h-4 w-4" />
          로그아웃
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  );
};

export default HeaderDropDown;
