import { Skeleton } from "@/shared/ui/skeleton"; // Skeleton 컴포넌트 import 필요
import { Card } from "@/shared/ui/card";

// ... existing code ...
const ProjectInfoCardSkeleton = () => {
  return (
    <Card className="flex h-full flex-col gap-4 p-4">
      <div className="flex items-center justify-between gap-4">
        <div className="flex items-center gap-2">
          <Skeleton className="h-10 w-10 rounded-full" />
          <Skeleton className="h-7 w-40" />
        </div>
        <Skeleton className="h-10 w-10" />
      </div>

      <div className="my-2 flex flex-col gap-2">
        <div className="text-md mb-2 flex items-center gap-1.5">
          <Skeleton className="h-5 w-5" />
          <Skeleton className="h-5 w-32" />
        </div>
        <div className="text-md mb-2 flex items-center gap-1.5">
          <Skeleton className="h-5 w-5" />
          <Skeleton className="h-5 w-40" />
        </div>
      </div>

      <div className="grid w-full grid-cols-3 gap-2">
        <Skeleton className="h-10 w-full" />
        <Skeleton className="h-10 w-full" />
        <Skeleton className="h-10 w-full" />
      </div>

      <hr className="my-2" />

      <div>
        <Skeleton className="mb-2 h-7 w-32" />
        <div className="flex flex-wrap gap-2">
          {[1, 2, 3].map((i) => (
            <div key={i} className="flex items-center gap-2">
              <Skeleton className="h-8 w-8 rounded-full" />
              <Skeleton className="h-8 w-24" />
            </div>
          ))}
        </div>
      </div>
    </Card>
  );
};

export default ProjectInfoCardSkeleton;
