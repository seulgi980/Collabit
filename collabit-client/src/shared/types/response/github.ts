export interface GithubUser {
  login: string;
  avatar_url: string;
}

export interface GithubOrg {
  login: string;
  avatar_url: string;
}

export interface GithubRepo {
  id: number;
  name: string;
  full_name: string;
  owner: GithubUser;
  description: string | null;
  created_at: string;
  updated_at: string;
  pushed_at: string;
  homepage: string | null;
  stargazers_count: number;
  watchers_count: number;
  language: string | null;
  forks_count: number;
  default_branch: string;
  visibility: string;
  html_url: string;
  open_issues_count: number;
}

export interface FormattedGithubRepo {
  title: string;
  organization: string;
  organizationImage: string;
  description: string | null;
  language: string | null;
  visibility: string;
  html_url: string;
  updated_at: string;
  stargazers_count: number;
  forks_count: number;
  open_issues_count: number;
}

export const formatGithubRepo = (repo: GithubRepo): FormattedGithubRepo => ({
  title: repo.name,
  organization: repo.owner.login,
  organizationImage: repo.owner.avatar_url,
  description: repo.description,
  language: repo.language,
  visibility: repo.visibility,
  html_url: repo.html_url,
  updated_at: repo.updated_at,
  stargazers_count: repo.stargazers_count,
  forks_count: repo.forks_count,
  open_issues_count: repo.open_issues_count,
});
