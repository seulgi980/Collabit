import ProjectContributor from "@/entities/project/ui/ProjectContributor";
import { Button } from "@/shared/ui/button";
import { Card, CardDescription, CardTitle } from "@/shared/ui/card";
import { LockIcon } from "lucide-react";
import { FormattedGithubRepo } from "@/shared/types/response/github";
import { useCreateProject } from "../api/useCreateProject";
import { useGithubContributors } from "../api/useGithubContributors";
import formatRelativeTime from "@/shared/utils/formatRelativeTime";

interface ProjectCreateCardProps {
  project: FormattedGithubRepo;
}

const ProjectCreateCard = ({ project }: ProjectCreateCardProps) => {
  const { handleCreateProject, isAdded } = useCreateProject(
    project.organization,
    project.title,
  );

  const { contributors, isLoading: isContributorsLoading } =
    useGithubContributors(project.organization, project.title);

  const timeAgo = formatRelativeTime(project.updatedAt);

  return (
    <Card className="flex h-[90px] items-center justify-between bg-violet-50 px-4 drop-shadow-lg">
      <div className="flex flex-row items-center justify-between gap-4">
        {isContributorsLoading ? (
          <div className="h-8 w-8 animate-pulse rounded-full bg-gray-200" />
        ) : (
          <ProjectContributor contributor={contributors || []} />
        )}
        <CardTitle className="text-lg md:text-xl">{project.title}</CardTitle>
      </div>
      <div className="flex items-center justify-between gap-2">
        {isAdded && (
          <Button variant="ghost" className="h-4 w-4">
            <LockIcon className="h-full w-full text-gray-400" />
          </Button>
        )}
        <CardDescription className="mr-4 hidden w-[50px] text-right text-gray-400 md:block">
          {timeAgo}
        </CardDescription>
        <Button
          className="bg-black"
          onClick={() =>
            handleCreateProject({
              ...project,
              contributors: contributors || [],
            })
          }
          disabled={isAdded}
        >
          {isAdded ? "추가됨" : "선택"}
        </Button>
      </div>
    </Card>
  );
};

export default ProjectCreateCard;
