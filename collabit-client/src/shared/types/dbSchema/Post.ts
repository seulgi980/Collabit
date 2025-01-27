export interface Post {
  code: number;
  userCode: string;
  content: string;
  createdAt: Date;
  updatedAt: Date;
}
export interface Image {
  code: number;
  postCode: number;
  url: string;
}
export interface PostLike {
  userCode: string;
  postCode: number;
}
