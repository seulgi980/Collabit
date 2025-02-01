import { Contributor, Project, ProjectInfo } from "../model/Project";

type ProjectInfoResponse = Pick<
  ProjectInfo,
  "code" | "total" | "participant" | "isDone" | "createdAt"
> &
  Pick<Project, "title">;

export type ProjectResponse = ProjectInfoResponse & {
  contributor: Contributor[];
};

export type ProjectListResponse = {
  organization: Project["organization"];
  organizationImage: Project["organizationImage"];
  projects: ProjectResponse[];
}[];
