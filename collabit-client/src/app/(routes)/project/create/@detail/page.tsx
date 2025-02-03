"use client";
import { useSearchParams } from "next/navigation";
import { useGithubRepos } from "@/features/project/api/useGithubRepos";
import { ProjectInfoCard } from "@/features/project/ui/ProjectInfoCard";

export default function DetailPage() {
  const searchParams = useSearchParams();
  const repoTitle = searchParams.get("repo");
  const { repos, isLoading } = useGithubRepos();

  if (!repoTitle) {
    return (
      <div className="flex h-full items-center justify-center">
        <p className="text-gray-500">프로젝트를 선택해주세요</p>
      </div>
    );
  }

  if (isLoading) {
    return (
      <div className="flex h-full items-center justify-center">
        <p className="text-gray-500">로딩 중...</p>
      </div>
    );
  }

  const selectedRepo = repos?.find((repo) => repo.title === repoTitle);

  if (!selectedRepo) {
    return (
      <div className="flex h-full items-center justify-center">
        <p className="text-gray-500">프로젝트를 찾을 수 없습니다</p>
      </div>
    );
  }

  return <ProjectInfoCard repo={selectedRepo} />;
}
