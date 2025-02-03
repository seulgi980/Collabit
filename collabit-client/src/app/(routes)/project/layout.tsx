import PageHeader from "@/entities/common/ui/PageHeader";

const ProjectLayout = ({ children }: { children: React.ReactNode }) => {
  return (
    <div className="mx-auto flex w-full max-w-5xl flex-col justify-center gap-5 py-10">
      <PageHeader
        mainTitle="나의 프로젝트"
        subTitle="동료들에게 피드백 받은 현황을 확인할 수 있어요."
      />
      {children}
    </div>
  );
};

export default ProjectLayout;