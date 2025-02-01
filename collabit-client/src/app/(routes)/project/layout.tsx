import PageHeader from "@/entities/common/ui/PageHeader";

interface ProjectLayoutProps {
  list: React.ReactNode;
}
const ProjectLayout = ({ list }: ProjectLayoutProps) => {
  return (
    <div className="mx-auto flex w-full max-w-5xl flex-col justify-center gap-5 py-10">
      <PageHeader
        mainTitle="나의 프로젝트"
        subTitle="동료들에게 피드백 받은 현황을 확인할 수 있어요."
      />
      {list}
    </div>
  );
};

export default ProjectLayout;
