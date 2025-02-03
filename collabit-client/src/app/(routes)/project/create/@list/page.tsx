"use client";
import SearchBar from "@/entities/common/ui/SearchBar";
import ProjectSelect from "@/entities/project/ui/ProjectSelect";
import ProjectSortSelect from "@/entities/project/ui/ProjectSort";
import { useGithubOrganization } from "@/features/project/api/useGithubOrganization";
import { useGithubOrgRepo } from "@/features/project/api/useGithubOrgRepo";
import { useGithubUserRepo } from "@/features/project/api/useGithubUserRepo";
import ProjectCreateCard from "@/features/project/ui/ProjectCreateCard";
import { FormattedGithubRepo } from "@/shared/types/response/github";
import { useState } from "react";
import { useSearchParams } from "next/navigation";
import { useAuth } from "@/features/auth/api/useAuth";

export default function ListPage() {
  const searchParams = useSearchParams();
  const selectedRepo = searchParams.get("repo");

  const { userInfo } = useAuth();
  const githubId = userInfo?.githubId || "";

  const [organization, setOrganization] = useState(githubId);
  const [keyword, setKeyword] = useState("");
  const [sort, setSort] = useState("recent");

  const { githubUserOrgs, isLoading: isGithubUserLoading } =
    useGithubOrganization(githubId ?? "");

  const isUserRepo = organization === githubId;
  const { githubUserRepos, isLoading: isUserRepoLoading } = useGithubUserRepo(
    isUserRepo ? organization : "",
  );
  const { githubOrgRepos, isLoading: isOrgRepoLoading } = useGithubOrgRepo(
    isUserRepo ? "" : organization,
  );

  const isLoading = isUserRepo ? isUserRepoLoading : isOrgRepoLoading;

  const repositories: FormattedGithubRepo[] = isUserRepo
    ? (githubUserRepos as FormattedGithubRepo[]) || []
    : (githubOrgRepos as FormattedGithubRepo[]) || [];

  const filteredRepos = repositories
    .filter((repo) => repo.title.toLowerCase().includes(keyword.toLowerCase()))
    .sort((a, b) => {
      const dateA = new Date(a.updated_at).getTime();
      const dateB = new Date(b.updated_at).getTime();

      return sort === "recent" ? dateB - dateA : dateA - dateB;
    });

  return (
    <div className="mx-auto flex w-full flex-col justify-center gap-5">
      {/* 모바일 레이아웃 */}
      <div className="flex flex-col gap-2 md:hidden">
        <div className="flex items-center justify-between gap-2">
          <ProjectSelect
            githubId={githubId}
            organizations={githubUserOrgs}
            organization={organization}
            isLoading={isGithubUserLoading}
            onOrganizationChange={setOrganization}
            className="w-1/2"
          />
          <ProjectSortSelect sort={sort} onSort={setSort} className="w-1/2" />
        </div>
        <SearchBar keyword={keyword} setKeyword={setKeyword} />
      </div>

      {/* 데스크톱 레이아웃 */}
      <div className="hidden md:flex md:items-center md:gap-2">
        <ProjectSelect
          githubId={githubId}
          organizations={githubUserOrgs}
          organization={organization}
          isLoading={isGithubUserLoading}
          onOrganizationChange={setOrganization}
          className="w-[280px]"
        />
        <SearchBar keyword={keyword} setKeyword={setKeyword} />
        <ProjectSortSelect sort={sort} onSort={setSort} className="w-[180px]" />
      </div>

      {isLoading ? (
        <p className="text-center">로딩 중...</p>
      ) : (
        <ul className="flex flex-col gap-2">
          {filteredRepos?.map((repo) => (
            <li key={repo.title}>
              <ProjectCreateCard
                project={repo}
                isSelected={repo.title === selectedRepo}
              />
            </li>
          ))}
        </ul>
      )}
    </div>
  );
}
