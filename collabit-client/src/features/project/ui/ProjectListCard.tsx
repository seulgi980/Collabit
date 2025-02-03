import ProjectCotnributor from "@/entities/project/ui/ProjectContributor";
import { Button } from "@/shared/ui/button";
import { Card, CardTitle } from "@/shared/ui/card";
import { Progress } from "@/shared/ui/progress";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/shared/ui/dropdown-menu";
import { DeleteIcon, EllipsisVertical, GithubIcon } from "lucide-react";
import { useProjectList } from "../api/useProjectList";
import { ProjectListItem } from "@/shared/types/model/Project";

interface ProjectListCardProps {
  project: ProjectListItem;
  onClick?: () => void;
}

const ProjectListCard = ({ project }: ProjectListCardProps) => {
  const { handleFinishSurvey, handleRemoveProject } = useProjectList();

  const contributorsCount = project.contributors.length;
  const participantsRatio = Math.floor(
    (project.participant * 100) / contributorsCount,
  );

  return (
    <Card className="flex cursor-pointer flex-col items-center justify-between gap-4 bg-violet-50 px-4 py-6 drop-shadow-lg">
      <div className="flex w-full items-center justify-between gap-10">
        <ProjectCotnributor contributor={project.contributors} />
        <DropdownMenu>
          <DropdownMenuTrigger
            className="flex h-10 w-10 items-center justify-center rounded-full bg-violet-300"
            onClick={(e) => e.stopPropagation()}
          >
            <EllipsisVertical />
          </DropdownMenuTrigger>
          <DropdownMenuContent>
            {/* 설문 참여 인원이 0명일 때만 프로젝트 삭제 가능*/}
            {project.participant == 0 && (
              <DropdownMenuItem
                onClick={(e) => {
                  e.stopPropagation();
                  handleRemoveProject(project.code);
                }}
                className="cursor-pointer text-red-500"
              >
                <DeleteIcon />
                프로젝트 삭제
              </DropdownMenuItem>
            )}
            <DropdownMenuItem
              onClick={() => window.open(`https://github.com/${project.title}`)}
              className="cursor-pointer"
            >
              <GithubIcon />
              github으로 이동
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>
      <div className="flex w-full items-center justify-between">
        <CardTitle className="text-xl">{project.title}</CardTitle>
        {project.isDone ? (
          <>
            <Button className="z-5 disabled bg-gray-400">종료됨</Button>
          </>
        ) : (
          <Button
            className="z-5 bg-black"
            onClick={(e) => {
              e.stopPropagation();
              handleFinishSurvey(project.code);
            }}
          >
            설문 종료
          </Button>
        )}
      </div>
      <div className="items-left flex w-full flex-col justify-center gap-2">
        <span className="text-sm">
          <span className="font-semibold">{project.contributors.length}</span>명
          중 <span className="font-semibold">{project.participant}</span>명 참여
          / <span className="font-semibold">{participantsRatio}</span>%
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
