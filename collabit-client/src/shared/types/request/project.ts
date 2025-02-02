import { Contributor, Project, ProjectInfo } from "../model/Project";

export type ProjectCreateRequest = Pick<
  Project,
  "title" | "organization" | "organizationImage"
> & {
  contributors: Contributor[];
};
export type ProjectRemoveRequest = Pick<ProjectInfo, "code">;
export type ProjectFinishRequest = Pick<ProjectInfo, "code">;
