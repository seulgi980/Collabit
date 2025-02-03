"use client";
import { Button } from "@/shared/ui/button";
import { ArrowLeftIcon } from "lucide-react";

interface PageHeaderProps {
  mainTitle: string;
  subTitle: string;
  handleBack?: () => void | null;
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
          <ArrowLeftIcon style={{ width: "20px", height: "20px" }} />
        </Button>
      )}

      <div className="flex flex-col items-center justify-center gap-1">
        <h1 className="text-center text-lg font-bold md:text-2xl">
          {mainTitle}
        </h1>
        <h2 className="md:text-md px-12 text-center text-sm text-gray-400">
          {subTitle}
        </h2>
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
