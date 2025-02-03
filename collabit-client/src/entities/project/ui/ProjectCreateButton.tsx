import { Button } from "@/shared/ui/button";
import { FormattedGithubRepo } from "@/shared/types/response/github";
import { useCreateProject } from "@/features/project/api/useCreateProject";
import { useGithubContributors } from "@/features/project/api/useGithub";

interface ProjectCreateButtonProps {
  project: FormattedGithubRepo;
}

export default function ProjectCreateButton({
  project,
}: ProjectCreateButtonProps) {
  const { contributors } = useGithubContributors(
    project.organization,
    project.title,
  );
  const { handleCreateProject, isAdded } = useCreateProject(
    project.organization,
    project.title,
  );

  const handleClick = (e: React.MouseEvent) => {
    e.stopPropagation();
    if (!isAdded) {
      handleCreateProject({
        ...project,
        contributors: contributors || [],
      });
    }
  };

  if (isAdded) {
    return (
      <Button
        variant="outline"
        className="w-[60px] cursor-not-allowed bg-gray-100 text-gray-400"
        disabled
      >
        완료
      </Button>
    );
  }

  return (
    <Button className="w-[60px] bg-black" onClick={handleClick}>
      등록
    </Button>
  );
}
