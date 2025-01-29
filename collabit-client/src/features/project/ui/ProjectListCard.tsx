import ProjectCotnributor from "@/entities/project/ui/ProjectContributor";
import { ProjectInfo } from "@/shared/types/model/Project";
import { Button } from "@/shared/ui/button";
import { Card, CardTitle } from "@/shared/ui/card";
import { Progress } from "@/shared/ui/progress";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/shared/ui/dropdown-menu";
import { EllipsisVertical } from "lucide-react";

interface ProjectListCardProps {
  project: ProjectInfo;
  onClick?: () => void;
}

const ProjectListCard = ({ project, onClick }: ProjectListCardProps) => {
  const contributorsCount = project.contributor.length;
  const participantsRatio = Math.floor(
    (project.participant * 100) / contributorsCount,
  );

  return (
    <Card className="flex cursor-pointer flex-col items-center justify-between gap-4 bg-violet-50 px-4 py-6 drop-shadow-lg">
      <div className="flex w-full items-center justify-between gap-10">
        <ProjectCotnributor contributor={project.contributor} />
        <DropdownMenu>
          <DropdownMenuTrigger
            className="flex h-10 w-10 items-center justify-center rounded-full bg-violet-300"
            onClick={(e) => e.stopPropagation()}
          >
            <EllipsisVertical />
          </DropdownMenuTrigger>
          <DropdownMenuContent>
            <DropdownMenuItem
              onClick={() => window.open(`https://github.com/${project.title}`)}
              className="cursor-pointer"
            >
              github으로 이동
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>
      <div className="flex w-full items-center justify-between">
        <CardTitle className="text-xl">{project.title}</CardTitle>
        {project.isDone ? (
          <Button className="bg-gray-400">종료됨</Button>
        ) : (
          <Button className="bg-black">설문 종료</Button>
        )}
      </div>
      <div className="items-left flex w-full flex-col justify-center gap-2">
        <span className="text-sm">
          <span className="font-semibold">{project.participant}</span>명 참여 /{" "}
          <span className="font-semibold">{participantsRatio}</span>%
        </span>
        <Progress
          className="h-1 bg-white [&>div]:rounded-full [&>div]:bg-black [&>div]:transition-all"
          value={participantsRatio}
        />
      </div>
    </Card>
  );
};

export default ProjectListCard;
