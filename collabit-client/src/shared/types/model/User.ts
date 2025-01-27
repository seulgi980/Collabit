export interface User {
  code: string;
  email: string;
  githubId: string;
  password: string;
  nickname: string;
  profileImage: string;
  createdAt: Date;
  role: string;
}
export type UserInfo = Pick<User, "nickname" | "profileImage" | "githubId">;
