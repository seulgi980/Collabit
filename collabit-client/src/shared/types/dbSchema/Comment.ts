export interface Comment {
  code: number;
  postCode: number;
  userCode: string;
  content: string;
  createdAt: Date;
  updatedAt: Date;
  parentCommentCode: number;
  isDeleted: boolean;
}
