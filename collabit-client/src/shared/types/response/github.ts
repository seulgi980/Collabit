export interface GithubUser {
  login: string;
  avatar_url: string;
}

export interface GithubOrg {
  login: string;
  avatar_url: string;
}

export interface GithubRepo {
  name: string;
  owner: GithubUser;
  updated_at: string;
}

export interface FormattedGithubRepo {
  organization: string;
  organizationImage: string;
  title: string;
  updatedAt: string;
  isAdded: boolean;
  contributors?: {
    githubId: string;
    profileImage: string;
  }[];
}

export const formatGithubRepo = (repo: GithubRepo): FormattedGithubRepo => ({
  organization: repo.owner.login,
  organizationImage: repo.owner.avatar_url,
  title: repo.name,
  updatedAt: repo.updated_at,
  isAdded: false,
});
