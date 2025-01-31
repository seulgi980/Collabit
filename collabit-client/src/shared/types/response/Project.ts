import { Contributor, Project } from "../model/Project";

export type ProjectUpdateCheckResponse = {
  isUpdated: boolean;
};

export type GithubRepoResponse = Pick<Project, "title" | "organization"> & {
  updatedAt: Date;
};

export type GithubOrgResponse = Pick<Project, "organization">;

export type GithubCollaboratorResponse = {
  contributors: Contributor[];
};
