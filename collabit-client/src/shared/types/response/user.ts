import { UserInfo } from "../model/User";

export interface UserInfoResponse {
  userInfo: UserInfo | undefined;
  isAuthenticated: boolean;
}
