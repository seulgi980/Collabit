import { getGithubUserReposAPI } from "@/shared/api/project";
import { GithubRepo, formatGithubRepo } from "@/shared/types/response/github";
import { useQuery } from "@tanstack/react-query";

export const useGithubUserRepo = (keyword: string) => {
  const { data: githubUserRepos, isLoading } = useQuery({
    queryKey: ["githubUserRepos", keyword],
    queryFn: () =>
      keyword ? getGithubUserReposAPI(keyword) : Promise.resolve([]),
    enabled: !!keyword,
    select: (data: GithubRepo[]) => data.map(formatGithubRepo),
  });

  return {
    githubUserRepos,
    isLoading,
  };
};
