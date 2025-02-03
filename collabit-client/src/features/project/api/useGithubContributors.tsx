import { getGithubCollaboratorsAPI } from "@/shared/api/github";
import { GithubUser } from "@/shared/types/response/github";
import { useQuery } from "@tanstack/react-query";

interface Contributor {
  githubId: string;
  profileImage: string;
}

export const useGithubContributors = (organization: string, title: string) => {
  const { data: contributors, isLoading } = useQuery({
    queryKey: ["githubContributors", organization, title],
    queryFn: () =>
      organization && title
        ? getGithubCollaboratorsAPI(organization, title)
        : Promise.resolve([]),
    enabled: !!organization && !!title,
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
