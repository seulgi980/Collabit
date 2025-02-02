import { Contributor, Project, ProjectInfo, ProjectTitle } from "../model/Project";

type ProjectInfoResponse = Pick<
  ProjectInfo,
  "code" | "total" | "participant" | "isDone" | "createdAt"
> &
  Pick<Project, "title">;
export type ProjectResponse = ProjectInfoResponse & {
  contributors: Contributor[];
};
export type ProjectListResponse = {
  organization: Project["organization"];
  organizationImage: Project["organizationImage"];
  projects: ProjectResponse[];
}[];
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
