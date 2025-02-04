import { GithubRepo, formatGithubRepo } from "@/shared/types/response/github";
import { useQuery } from "@tanstack/react-query";

// 받는 키워드와 함수에 따라 개인 레포, 오가니제이션 둘 다 호환
export const useGetGithubRepoList = ({
  keyword,
  direction,
  api,
}: {
  keyword: string;
  direction: "asc" | "desc";
  api: (params: {
    keyword: string;
    direction: "asc" | "desc";
  }) => Promise<GithubRepo[]>;
}) => {
  const { data, isLoading } = useQuery({
    queryKey: ["githubUserRepos", keyword, direction],
    queryFn: () => api({ keyword, direction }),
    select: (data: GithubRepo[]) => data.map(formatGithubRepo),
  });

  return {
    data,
    isLoading,
  };
};
