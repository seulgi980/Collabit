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
export interface ProjectContributor {
  projectCode: number;
  projectInfoCode: number;
  githubId: string;
}
