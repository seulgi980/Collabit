export interface Project {
  code: number;
  title: string;
}
export interface ProjectInfo {
  code: number;
  projectCode: number;
  userCode: string;
  total: number;
  participant: number;
  totalScore: number;
  idDone: boolean;
}

export interface Contributor {
  code: number;
  id: string;
  profileImage: string;
}
export interface ProjectContributor {
  projectCode: number;
  projectInfoCode: number;
  contributorCode: number;
}
