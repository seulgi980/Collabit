export interface Project {
export interface Project {
  code: number;
  title: string;
  organization: string;
  organizationImage: string;
}
export interface ProjectInfo {
  code: number;
  postCode: number;
  userCode: string;
  projectCode: number;
  userCode: string;
  total: number;
  participant: number;
  isDone: boolean;
  createdAt: string;
  sympathy: number;
  listening: number;
  expression: number;
  problemSolving: number;
  conflictResolution: number;
  leadership: number;
}
export interface Contributor {
  githubId: string;
  profileImage: string;
}
export type ProjectListItem = Pick<Project, "title" | "organization"> &
  Pick<ProjectInfo, "code" | "total" | "participant" | "isDone"> & {
    contributors: Contributor[];
    isUpdated: boolean;
  };
export type ProjectTitle = Pick<Project, "title" | "organization">;
export interface ProjectContributor {
  projectCode: number;
  projectInfoCode: number;
  githubId: string;
}
