import { Skeleton } from "@/shared/ui/skeleton";

const ProjectListSkeleton = () => {
  return (
    <div className="space-y-4">
      {/* 3개의 아코디언 아이템을 보여주는 스켈레톤 */}
      {[1, 2, 3].map((item) => (
        <div key={item}>
          {/* 아코디언 헤더 스켈레톤 */}
          <div className="flex items-center gap-2">
            {/* 아바타 스켈레톤 */}
            <Skeleton className="h-10 w-10 rounded-full" />
            {/* 조직명 스켈레톤 */}
            <Skeleton className="h-6 w-40" />
          </div>

          {/* 아코디언 컨텐츠 스켈레톤 */}
          <div className="mt-4 grid grid-cols-1 gap-4 md:grid-cols-2">
            {/* 프로젝트 카드 스켈레톤 4개 */}
            {[1, 2, 3, 4].map((card) => (
              <Skeleton key={card} className="h-[120px] w-full rounded-lg" />
            ))}
          </div>
        </div>
      ))}
    </div>
  );
};

export default ProjectListSkeleton;
