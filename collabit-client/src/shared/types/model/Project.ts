export interface Project {
  code: number;
  title: string;
  organization: string;
}
export interface ProjectInfo {
  code: number;
  projectCode: number;
  userCode: string;
  total: number;
  participant: number;
  isDone: boolean;
  sympathy: number;
  listening: number;
  expression: number;
  problemSolving: number;
  conflictResolution: number;
  leadership: number;
}
export interface Contributor {
  code: number;
  githubId: string;
  profileImage: string;
}
export type ProjectListItem = Pick<Project, "title" | "organization"> &
  Pick<ProjectInfo, "code" | "total" | "participant" | "isDone"> & {
    contributors: Contributor[];
    isUpdated: boolean;
  };