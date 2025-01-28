"use client";
import { Button } from "@/shared/ui/button";
import { ArrowLeftIcon, PlusIcon } from "lucide-react";
import { useRouter } from "next/navigation";

interface ProjectHeaderProps {
  mainTitle: string;
  subTitle: string;
  isList: boolean;
}

const ProjectHeader = ({ mainTitle, subTitle, isList }: ProjectHeaderProps) => {
  const router = useRouter();
  const handleBack = () => {
    router.push("/project");
  };
  const handleCreateProject = () => {
    router.push("/project/create");
  };
  return (
    <div className="flex items-center justify-between">
      <Button variant="ghost" className="h-8 w-8" onClick={handleBack}>
        <ArrowLeftIcon className="h-full w-full" />
      </Button>
      <div className="flex flex-col items-center justify-center gap-1">
        <h1 className="text-xl font-bold">{mainTitle}</h1>
        <h2 className="text-xs text-gray-400">{subTitle}</h2>
      </div>
      <Button
        variant="ghost"
        className={`h-8 w-8 ${isList ? "" : "invisible"}`}
        onClick={handleCreateProject}
      >
        <PlusIcon className="h-full w-full" />
      </Button>
    </div>
  );
};

export default ProjectHeader;
