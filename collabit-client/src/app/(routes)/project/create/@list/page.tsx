"use client";
import SearchBar from "@/entities/common/ui/SearchBar";
import { useGithubOrganization } from "@/features/project/api/useGithubOrganization";
import { useGithubOrgRepo } from "@/features/project/api/useGithubOrgRepo";
import { useGithubUserRepo } from "@/features/project/api/useGithubUserRepo";
import ProjectCreateCard from "@/features/project/ui/ProjectCreateCard";
import { FormattedGithubRepo } from "@/shared/types/response/github";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/shared/ui/select";
import { useState } from "react";

export default function Page() {
  const githubId = "seon318";

  const { githubUserOrgs, isLoading: isGithubUserLoading } =
    useGithubOrganization(githubId);

  const [organization, setOrganization] = useState(githubId);
  const [keyword, setKeyword] = useState("");

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

  const filteredRepos = repositories.filter((repo) =>
    repo.title.toLowerCase().includes(keyword.toLowerCase()),
  );

  const handleOrganizationChange = (value: string) => {
    setOrganization(value);
  };

  return (
    <div className="mx-auto flex w-full flex-col justify-center gap-5 py-10 md:w-[540px]">
      <div className="flex items-center justify-between gap-2">
        <Select
          defaultValue={organization}
          onValueChange={handleOrganizationChange}
        >
          <SelectTrigger className="w-[120px]">
            <SelectValue placeholder="조직" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value={githubId}>{githubId}</SelectItem>
            {/* {isGithubUserLoading ? (
              <SelectItem value="loading" disabled>
                로딩 중...
              </SelectItem>
            ) : (
              githubUserOrgs?.map((org) => (
                <SelectItem key={org.organization} value={org.organization}>
                  {org.organization}
                </SelectItem>
              ))
            )} */}
          </SelectContent>
        </Select>
        <SearchBar keyword={keyword} setKeyword={setKeyword} />
      </div>
      {/* 
      {isLoading ? (
        <p className="text-center">로딩 중...</p>
      ) : (
        filteredRepos.map((repo) => (
          <ProjectCreateCard key={repo.title} project={repo} />
        ))
      )} */}
    </div>
  );
}
