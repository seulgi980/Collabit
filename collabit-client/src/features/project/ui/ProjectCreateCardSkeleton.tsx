import { Card } from "@/shared/ui/card";
import { Skeleton } from "@/shared/ui/skeleton";

const ProjectCreateCardSkeleton = () => {
  return (
    <div className="flex flex-col gap-2">
      {[1, 2, 3, 4, 5].map((i) => (
        <Card
          key={i}
          className="flex h-[60px] w-full items-center justify-between bg-violet-50 px-4"
        >
          <div className="flex flex-row items-center justify-between gap-4">
            <Skeleton className="h-6 w-6 rounded-full" />
            <Skeleton className="h-5 w-40" />
          </div>
          <div className="flex items-center justify-between gap-2">
            <Skeleton className="h-4 w-20" />
            <Skeleton className="h-8 w-24" />
          </div>
        </Card>
      ))}
    </div>
  );
};

export default ProjectCreateCardSkeleton;
