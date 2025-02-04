"use client";
import { useSearchParams } from "next/navigation";

import { ProjectInfoCard } from "@/features/project/ui/ProjectInfoCard";
import useGetGithubRepoDetailAPI from "@/features/project/api/useGetGithubRepoDetailAPI";
import ProjectInfoCardSkeleton from "@/features/project/ui/ProjectInfoCardSkeleton";

export default function DetailPage() {
  const searchParams = useSearchParams();
  const keyword = searchParams.get("keyword");
  const title = searchParams.get("repo");

  const { data, isLoading } = useGetGithubRepoDetailAPI(keyword!, title!);

  if (searchParams.size === 0) {
    return (
      <div className="flex h-full flex-col items-center justify-center gap-4 p-8">
        <div className="rounded-full bg-gray-100 p-6">
          <svg
            className="h-12 w-12 text-gray-400"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth={2}
              d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
            />
          </svg>
        </div>
        <div className="text-center">
          <h3 className="mb-2 text-lg font-semibold text-gray-900">
            프로젝트를 선택해주세요
          </h3>
          <p className="text-sm text-gray-500">
            왼쪽 목록에서 프로젝트를 선택하시면
            <br />
            상세 정보를 확인하실 수 있습니다
          </p>
        </div>
      </div>
    );
  }
  if (isLoading) {
    return <ProjectInfoCardSkeleton />;
  }
  if (!data) {
    return (
      <div className="flex h-full items-center justify-center">
        <p className="text-gray-500">프로젝트를 찾을 수 없습니다</p>
      </div>
    );
  }
  return <ProjectInfoCard repo={data} />;
}
