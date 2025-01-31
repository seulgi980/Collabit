import ProjectCotnributor from "@/entities/project/ui/ProjectContributor";
import { Button } from "@/shared/ui/button";
import { Card, CardDescription, CardTitle } from "@/shared/ui/card";
import { LockIcon } from "lucide-react";
import {
  GithubCollaboratorResponse,
  GithubRepoResponse,
} from "@/shared/types/response/Project";
import { useGithubProject } from "../api/useGithubProject";

interface ProjectCreateCardProps {
  project: GithubRepoResponse & GithubCollaboratorResponse;
}

const ProjectCreateCard = ({ project }: ProjectCreateCardProps) => {
  const { handleCreateProject } = useGithubProject();

  const now = new Date();
  const diffInMilliseconds = now.getTime() - project.updatedAt.getTime();

  const hours = Math.floor(diffInMilliseconds / 3600000);
  const days = Math.floor(hours / 24);
  const months = Math.floor(days / 30);
  const years = Math.floor(days / 365);

  let timeAgo = "";

  if (years > 0) {
    timeAgo = `${years}년 전`;
  } else if (months > 0) {
    timeAgo = `${months}달 전`;
  } else if (days > 0) {
    timeAgo = `${days}일 전`;
  } else if (hours > 0) {
    timeAgo = `${hours}시간 전`;
  }
  return (
    <Card className="flex h-[90px] items-center justify-between bg-violet-50 px-4 drop-shadow-lg">
      <div className="flex flex-row items-center justify-between gap-4">
        <ProjectCotnributor contributor={project.contributors} />
        <CardTitle className="text-lg md:text-xl">{project.title}</CardTitle>
      </div>
      <div className="flex items-center justify-between">
        <Button variant="ghost" className="h-4 w-4">
          <LockIcon className="h-full w-full text-gray-400" />
        </Button>
        <CardDescription className="mr-4 hidden w-[50px] text-right text-gray-400 md:block">
          {timeAgo}
        </CardDescription>
        <Button
          className="bg-black"
          onClick={() => handleCreateProject(project)}
        >
          선택
        </Button>
      </div>
    </Card>
  );
};

export default ProjectCreateCard;
