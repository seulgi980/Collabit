import {
  getGithubUserReposAPI,
  getGithubOrgReposAPI,
  getGithubUserOrgsAPI,
  getGithubCollaboratorsAPI,
} from "@/shared/api/github";
import { useQuery } from "@tanstack/react-query";
import {
  GithubRepo,
  GithubOrg,
  GithubUser,
  formatGithubRepo,
} from "@/shared/types/response/github";
import { Contributor } from "@/shared/types/model/Project";

export const useGithubRepos = (organization: string, isUserRepo: boolean) => {
  const { data: repos, isLoading } = useQuery({
    queryKey: ["githubRepos", organization, isUserRepo],
    queryFn: () =>
      isUserRepo
        ? getGithubUserReposAPI(organization)
        : getGithubOrgReposAPI(organization),
    enabled: !!organization,
    select: (data: GithubRepo[]) => data.map(formatGithubRepo),
  });

  return { repos, isLoading };
};

export const useGithubOrganizations = (githubId: string) => {
  const { data: organizations, isLoading } = useQuery({
    queryKey: ["githubOrgs", githubId],
    queryFn: () => getGithubUserOrgsAPI(githubId),
    enabled: !!githubId,
    select: (data: GithubOrg[]) =>
      data.map((org) => ({
        organization: org.login,
        organizationImage: org.avatar_url,
      })),
  });

  return { organizations, isLoading };
};

export const useGithubContributors = (org: string, repo: string) => {
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
