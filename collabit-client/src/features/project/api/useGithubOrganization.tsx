import { getGithubUserOrgsAPI } from "@/shared/api/project";
import { GithubOrg } from "@/shared/types/response/github";
import { useQuery } from "@tanstack/react-query";

export const useGithubOrganization = (keyword: string) => {
  const { data: githubUserOrgs, isLoading } = useQuery({
    queryKey: ["githubUserOrgs", keyword],
    queryFn: () =>
      keyword ? getGithubUserOrgsAPI(keyword) : Promise.resolve([]),
    enabled: !!keyword,
    select: (data: GithubOrg[]) =>
      data.map((org) => ({
        organization: org.login,
        organizationImage: org.avatar_url,
      })),
  });

  return {
    githubUserOrgs,
    isLoading,
  };
};
