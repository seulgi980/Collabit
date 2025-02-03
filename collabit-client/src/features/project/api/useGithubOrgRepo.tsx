import { getGithubOrgReposAPI } from "@/shared/api/project";
import { GithubRepo, formatGithubRepo } from "@/shared/types/response/github";
import { useQuery } from "@tanstack/react-query";

export const useGithubOrgRepo = (keyword: string) => {
  const { data: githubOrgRepos, isLoading } = useQuery({
    queryKey: ["githubOrgRepos", keyword],
    queryFn: () =>
      keyword ? getGithubOrgReposAPI(keyword) : Promise.resolve([]),
    enabled: !!keyword,
    select: (data: GithubRepo[]) => data.map(formatGithubRepo),
  });

  return {
    githubOrgRepos,
    isLoading,
  };
};
