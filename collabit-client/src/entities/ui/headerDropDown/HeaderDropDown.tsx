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

interface HeaderDropDownProps {
  handleToMyPage: () => void;
  handleLogout: () => void;
  Icon: ComponentType;
}

const HeaderDropDown = ({
  Icon,
  handleToMyPage,
  handleLogout,
}: HeaderDropDownProps) => {
  return (
    <DropdownMenu>
      <DropdownMenuTrigger>
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
