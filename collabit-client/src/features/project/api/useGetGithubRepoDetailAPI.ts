import { getGithubRepoDetailAPI } from "@/shared/api/github";
import { formatGithubRepo, GithubRepo } from "@/shared/types/response/github";
import { useQuery } from "@tanstack/react-query";

const useGetGithubRepoDetailAPI = (keyword: string, title: string) => {
  const enabled = Boolean(keyword && title);
  const { data, isLoading } = useQuery({
    queryKey: ["githubRepoDetail", keyword, title],
    queryFn: () => getGithubRepoDetailAPI(keyword, title),
    enabled,
    select: (data: GithubRepo) => formatGithubRepo(data),
  });

  return { data, isLoading };
};

export default useGetGithubRepoDetailAPI;
