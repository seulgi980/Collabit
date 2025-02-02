"use client";
import PageHeader from "@/entities/common/ui/PageHeader";

interface ProjectCreateLayoutProps {
  list: React.ReactNode;
  detail: React.ReactNode;
}
const ProjectLayout = ({ list, detail }: ProjectCreateLayoutProps) => {
  return (
    <div className="mx-auto flex w-full max-w-5xl flex-col justify-center gap-5 py-10">
      <PageHeader
        mainTitle="프로젝트 등록"
        subTitle="프로젝트를 등록하고, 동료들에게 피드백을 요청해보세요."
      />
      <div className="flex">
        {list}
        {detail}
      </div>
    </div>
  );
};

export default ProjectLayout;
