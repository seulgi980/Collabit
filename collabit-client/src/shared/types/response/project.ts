import { Contributor, Project, ProjectTitle } from "../model/Project";

export type ProjectUpdateCheckResponse = {
  isUpdated: boolean;
};
export type GithubRepoResponse = ProjectTitle & {
  updatedAt: Date;
};
export type GithubOrgResponse = Pick<Project, "organization">;
export type GithubCollaboratorResponse = {
  contributors: Contributor[];
};
export type ProjectAddedResponse = ProjectTitle & {
  isAdded: boolean;
  contributors: Contributor[];
};
