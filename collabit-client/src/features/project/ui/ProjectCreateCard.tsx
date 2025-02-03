import { Card, CardDescription, CardTitle } from "@/shared/ui/card";
import { FormattedGithubRepo } from "@/shared/types/response/github";
import formatRelativeTime from "@/shared/utils/formatRelativeTime";
import { useRouter } from "next/navigation";
import Image from "next/image";
import { cn } from "@/lib/utils";
import ProjectCreateButton from "@/entities/project/ui/ProjectCreateButton";

interface ProjectCreateCardProps {
  project: FormattedGithubRepo;
  isSelected?: boolean;
}

const ProjectCreateCard = ({ project, isSelected }: ProjectCreateCardProps) => {
  const router = useRouter();
  const timeAgo = formatRelativeTime(project.updated_at);

  return (
    <Card
      className={cn(
        "flex h-[60px] cursor-pointer items-center justify-between px-4 transition-colors",
        isSelected
          ? "bg-violet-200 hover:bg-violet-300"
          : "bg-violet-50 hover:bg-violet-100",
      )}
      onClick={() => {
        router.push(`/project/create?repo=${project.title}`);
      }}
    >
      <div className="flex flex-row items-center justify-between gap-4">
        <Image
          width={24}
          height={24}
          src="/images/github-profile.png"
          alt="Github Profile"
        />
        <CardTitle className="text-md md:text-lg">{project.title}</CardTitle>
      </div>
      <div className="flex items-center justify-between gap-2">
        <CardDescription className="mr-4 hidden w-[100px] text-right text-gray-500 md:block">
          {timeAgo}
        </CardDescription>
        <ProjectCreateButton project={project} />
      </div>
    </Card>
  );
};

export default ProjectCreateCard;
