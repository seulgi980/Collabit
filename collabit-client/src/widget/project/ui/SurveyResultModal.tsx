import ProjectListCard from "@/features/project/ui/ProjectListCard";
import { ProjectResponse } from "@/shared/types/response/project";
import { Button } from "@/shared/ui/button";
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogOverlay,
  DialogPortal,
  DialogTitle,
  DialogTrigger,
} from "@radix-ui/react-dialog";
import { X } from "lucide-react";

interface SurveyResultModalProps {
  project: ProjectResponse;
  organization: string;
}

const SurveyResultModal = ({
  project,
  organization,
}: SurveyResultModalProps) => {
  return (
    <Dialog>
      <DialogTrigger asChild>
        <div>
          <ProjectListCard organization={organization} project={project} />
        </div>
      </DialogTrigger>
      <DialogPortal>
        <DialogOverlay className="fixed inset-0 bg-black/50" />
        <DialogContent className="fixed left-1/2 top-1/2 z-50 w-full max-w-md -translate-x-1/2 -translate-y-1/2 rounded-lg bg-white p-6 shadow-lg">
          <DialogTitle className="text-center text-lg font-bold">
            {project.title}
          </DialogTitle>
          <DialogDescription className="mb-8 mt-1 text-center text-xs text-gray-400">
            해당 프로젝트에서 동료들이 평가한 결과입니다.
          </DialogDescription>
          <div className="items-left flex flex-col justify-center">
            <h1 className="text-xl font-bold">전체 역량 분석</h1>
            <div className="flex h-[200px] items-center justify-center">
              역량 분석 내용입니다.
            </div>
          </div>
          <div className="flex flex-col items-center justify-center">
            <h1 className="text-center text-xl font-bold">
              사용자 평균 대비 개인 역량도
            </h1>
            <div className="flex h-[200px] items-center justify-center">
              역량도 그래프입니다.
            </div>
          </div>
          <DialogClose asChild className="fixed right-3 top-3">
            <Button
              type="button"
              variant="ghost"
              className="h-8 w-8 border-none"
            >
              <X className="h-8 w-8" />
            </Button>
          </DialogClose>
        </DialogContent>
      </DialogPortal>
    </Dialog>
  );
};

export default SurveyResultModal;
