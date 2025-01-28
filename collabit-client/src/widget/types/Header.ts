export interface HeaderProps {
  isLogin: boolean;
  handleToLogin: () => void;
  handleLogout: () => void;
  handleToMyPage: () => void;
  menuList?: { name: string; href: string }[];
  hasNewChat?: boolean;
}
