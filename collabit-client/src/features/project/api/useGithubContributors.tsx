import { getGithubCollaboratorsAPI } from "@/shared/api/project";
import { GithubUser } from "@/shared/types/response/github";
import { useQuery } from "@tanstack/react-query";

interface Contributor {
  githubId: string;
  profileImage: string;
}

export const useGithubContributors = (org: string, title: string) => {
  const { data: contributors, isLoading } = useQuery({
    queryKey: ["githubContributors", org, title],
    queryFn: () =>
      org && title
        ? getGithubCollaboratorsAPI(org, title)
        : Promise.resolve([]),
    enabled: !!org && !!title,
    select: (data: GithubUser[]): Contributor[] =>
      data.map((user) => ({
        githubId: user.login,
        profileImage: user.avatar_url,
      })),
  });

  return {
    contributors,
    isLoading,
  };
};
