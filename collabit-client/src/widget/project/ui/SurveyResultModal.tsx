import ProjectListCard from "@/features/project/ui/ProjectListCard";
import SurveyResultCard from "@/features/project/ui/SurveyResultCard";
import { ProjectResponse } from "@/shared/types/response/project";

import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogPortal,
  DialogTitle,
  DialogTrigger,
} from "@/shared/ui/dialog";

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
        <DialogContent className="fixed z-50 max-h-[90vh] w-full max-w-[600px] -translate-x-1/2 -translate-y-1/2 overflow-y-auto rounded-lg bg-white p-6 shadow-lg md:max-w-[700px] lg:max-w-[800px]">
          <DialogTitle className="text-center text-lg font-bold">
            프로젝트 설문 결과
          </DialogTitle>
          <DialogDescription className="mb-8 mt-1 text-center text-xs text-gray-400">
            {project.title} 프로젝트에서 동료들이 평가한 결과입니다.
          </DialogDescription>

          <div className="flex flex-col gap-4">
            <SurveyResultCard code={project.code} />
          </div>
        </DialogContent>
      </DialogPortal>
    </Dialog>
  );
};

export default SurveyResultModal;
