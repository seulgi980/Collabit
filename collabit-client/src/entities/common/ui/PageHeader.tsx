"use client";
import { Button } from "@/shared/ui/button";
import { ArrowLeftIcon } from "lucide-react";

interface PageHeaderProps {
  mainTitle: string;
  subTitle: string;
  handleBack?: () => void;
  rightButton?: React.ReactNode;
}

const PageHeader = ({
  mainTitle,
  subTitle,
  handleBack,
  rightButton,
}: PageHeaderProps) => {
  return (
    <div className="relative flex items-center justify-center">
      {handleBack && (
        <Button
          variant="ghost"
          className="absolute left-0"
          onClick={handleBack}
        >
          <ArrowLeftIcon style={{ width: "24px", height: "24px" }} />
        </Button>
      )}

      <div className="flex flex-col items-center justify-center gap-1">
        <h1 className="text-3xl font-bold">{mainTitle}</h1>
        <h2 className="text-lg text-gray-400">{subTitle}</h2>
      </div>
      {/* <Button
        variant="ghost"
        className={`h-8 w-8 ${isList ? "" : "invisible"}`}
        onClick={handleCreateProject}
      >
        <PlusIcon className="h-full w-full" />
      </Button> */}
      <div className="absolute right-0">{rightButton}</div>
    </div>
  );
};

export default PageHeader;
