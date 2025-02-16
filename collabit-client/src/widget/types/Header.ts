import { UserInfo } from "@/shared/types/model/User";

export interface HeaderProps {
  isLogin: boolean;
  handleToLogin: () => void;
  handleLogout: () => void;
  handleToMyPage: () => void;
  menuList?: { name: string; href: string }[];
  hasNewChat?: boolean;
  userInfo?: UserInfo;
  isChatRoom?: boolean;
  hasNewResponse?: boolean;
}
