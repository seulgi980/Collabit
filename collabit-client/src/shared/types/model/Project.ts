export interface ProjectCreate {
  code: number;
  title: string;
  contributor: Contributor[];
  timestamp: Date;
}
export interface ProjectInfo {
  code: number;
  title: string;
  total: number;
  participant: number;
  isDone: boolean;
  surveyUrl: string;
  contributor: Contributor[];
}

export interface Contributor {
  code: number;
  githubId: string;
  profileImage: string;
}
