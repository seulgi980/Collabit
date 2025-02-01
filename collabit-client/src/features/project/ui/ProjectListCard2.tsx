import ProjectCotnributor from "@/entities/project/ui/ProjectContributor";
import useModalStore from "@/shared/lib/stores/modalStore";
import { ProjectResponse } from "@/shared/types/response/project";
import { Button } from "@/shared/ui/button";
import { Card, CardDescription, CardTitle } from "@/shared/ui/card";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/shared/ui/dropdown-menu";
import { Progress } from "@/shared/ui/progress";
import formatRelativeTime from "@/shared/utils/formatRelativeTime";
import TwoButtonModal from "@/widget/ui/modals/TwoButtonModal";
import { DeleteIcon, Ellipsis, GithubIcon } from "lucide-react";

interface ProjectListCardProps {
  project: ProjectResponse;
  onClick?: () => void;
}

const ProjectListCard2 = ({ project }: ProjectListCardProps) => {
  const { openModal } = useModalStore();

  const contributorsCount = project.contributor.length;
  const participantsRatio = Math.floor(
    (project.participant * 100) / contributorsCount,
  );

  const handleRemoveProject = (code: number) => {
    openModal(
      <TwoButtonModal
        title="설문을 삭제하시겠습니까?"
        description="이 프로젝트의 설문에 더이상 참여할 수 없습니다."
        confirmText="설문 삭제"
        cancelText="취소"
        handleConfirm={() => handleFinishSurvey(code)}
      />,
    );
  };

  const handleFinishSurvey = (code: number) => {
    console.log(code);
    openModal(
      <TwoButtonModal
        title="설문을 종료하시겠습니까?"
        description="이 프로젝트의 설문에 더이상 참여할 수 없습니다."
        confirmText="설문 종료"
        cancelText="취소"
        handleConfirm={() => handleFinishSurvey(code)}
      />,
    );
  };

  return (
    <Card className="flex cursor-pointer flex-col items-center justify-between gap-3 bg-violet-50 px-4 py-4 drop-shadow-lg">
      <div className="flex w-full items-center justify-between">
        <div className="flex items-center gap-3">
          <CardTitle className="text-lg">{project.title}</CardTitle>
          <CardDescription className="text-xs">
            {formatRelativeTime(project.createdAt)}
          </CardDescription>
        </div>
        <DropdownMenu>
          <DropdownMenuTrigger
            className="z-10 flex h-5 w-5 items-center justify-center text-gray-400"
            onClick={(e) => e.stopPropagation()}
          >
            <Ellipsis />
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
      <div className="flex w-full items-center justify-between gap-10">
        <div className="items-left flex w-full flex-col justify-center gap-2">
          <div className="flex items-center">
            <ProjectCotnributor size="sm" contributor={project.contributor} />
            <span className="text-sm">
              <span className="font-semibold">
                {project.contributor.length}
              </span>
              명 중 <span className="font-semibold">{project.participant}</span>
              명 참여 /{" "}
              <span className="font-semibold">{participantsRatio}</span>%
            </span>
          </div>
          <Progress
            className="h-1 bg-white [&>div]:rounded-full [&>div]:bg-black [&>div]:transition-all"
            value={participantsRatio}
          />
        </div>
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
    </Card>
  );
};

export default ProjectListCard2;
