import { Contributor, Project } from "../model/Project";

export type ProjectCreateRequest = Pick<
  Project,
  "title" | "organization" | "organizationImage"
> & {
  contributors: Contributor[];
};
