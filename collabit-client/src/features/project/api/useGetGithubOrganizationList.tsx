import { getGithubUserOrgsAPI } from "@/shared/api/github";
import { GithubOrg } from "@/shared/types/response/github";
import { useQuery } from "@tanstack/react-query";

export const useGetGithubOrganizationList = (
  keyword: string,
): {
  githubUserOrgs: { organization: string; organizationImage: string }[];
  isLoading: boolean;
} => {
  const { data: githubUserOrgs, isLoading } = useQuery({
    queryKey: ["githubUserOrgs", keyword],
    queryFn: () => getGithubUserOrgsAPI(keyword),
    enabled: !!keyword,
    select: (data: GithubOrg[]) =>
      data.map((org) => ({
        organization: org.login,
        organizationImage: org.avatar_url,
      })),
  });

  return {
    githubUserOrgs: githubUserOrgs ?? [],
    isLoading,
  };
};
