export interface Comment {
  code: number;
  postCode: number;
  userCode: string;
  content: string;
  createdAt: Date;
  updatedAt: Date;
  parentCode: number;
  isDeleted: boolean;
}
