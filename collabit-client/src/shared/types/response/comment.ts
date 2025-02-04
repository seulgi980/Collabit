import { Comment } from "../model/Comment";
import { User } from "../model/User";

export type CommentResponse = Pick<
  Comment,
  "code" | "postCode" | "content" | "createdAt"
> & {
  author: Pick<User, "nickname" | "profileImage">;
  children?: CommentResponse[];
};
export type CreateCommentResponse = Pick<
  Comment,
  "code" | "postCode" | "content" | "createdAt" | "updatedAt" | "parentCode"
>;
export type EditCommentResponse = CreateCommentResponse;
