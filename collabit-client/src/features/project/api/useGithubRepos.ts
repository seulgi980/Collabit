import { useGithubUserRepo } from "./useGithubUserRepo";
import { useGithubOrgRepo } from "./useGithubOrgRepo";
import { FormattedGithubRepo } from "@/shared/types/response/github";

export const useGithubRepos = () => {
  const { githubUserRepos } = useGithubUserRepo("seon318");
  const { githubOrgRepos } = useGithubOrgRepo("");

  const repos: FormattedGithubRepo[] = [
    ...(githubUserRepos || []),
    ...(githubOrgRepos || []),
  ];

  return { repos };
};
