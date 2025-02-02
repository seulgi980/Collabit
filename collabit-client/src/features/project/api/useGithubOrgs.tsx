import {
  getGithubOrgReposAPI,
  getGithubUserOrgsAPI,
} from "@/shared/api/project";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";

export const useGithubOrgs = (keyword: string, type: string) => {
  const [orgData, setOrgData] = useState<any>(null);

  const { data: githubOrgRepos, isLoading: isOrgReposLoading } = useQuery({
    queryKey: ["githubOrgRepos", keyword],
    queryFn: () =>
      keyword ? getGithubOrgReposAPI(keyword) : Promise.resolve([]),
    enabled: type === "org" && !!keyword,
  });

  const { data: githubUserOrgs, isLoading: isUserOrgsLoading } = useQuery({
    queryKey: ["githubUserOrgs", keyword],
    queryFn: () =>
      keyword ? getGithubUserOrgsAPI(keyword) : Promise.resolve([]),
    enabled: type === "id" && !!keyword,
  });

  useEffect(() => {
    if (githubOrgRepos) {
      console.log("Github Org Repos:", githubOrgRepos);
      setOrgData(githubOrgRepos);
    }

    if (githubUserOrgs) {
      console.log("Github User Orgs:", githubUserOrgs);
      setOrgData(githubUserOrgs);
    }
  }, [githubOrgRepos, githubUserOrgs]);

  return {
    githubOrgRepos,
    githubUserOrgs,
    isLoading: isOrgReposLoading || isUserOrgsLoading,
  };
};
