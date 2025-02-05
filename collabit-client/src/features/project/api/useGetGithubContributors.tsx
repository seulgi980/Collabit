import { getGithubCollaboratorsAPI } from "@/shared/api/github";
import { Contributor } from "@/shared/types/model/Project";
import { GithubUser } from "@/shared/types/response/github";
import { useQuery } from "@tanstack/react-query";

const useGetGithubContributors = (org: string, repo: string) => {
  const { data: contributors, isLoading } = useQuery({
    queryKey: ["githubContributors", org, repo],
    queryFn: () => getGithubCollaboratorsAPI(org, repo),
    enabled: !!org && !!repo,
    select: (data: GithubUser[]): Contributor[] =>
      data.map((user) => ({
        githubId: user.login,
        profileImage: user.avatar_url,
      })),
  });

  return { contributors, isLoading };
};

export default useGetGithubContributors;
