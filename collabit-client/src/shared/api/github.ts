const githubUrl = "https://api.github.com";
const clientId = process.env.NEXT_PUBLIC_GITHUB_CLIENT_ID;
const clientSecret = process.env.NEXT_PUBLIC_GITHUB_CLIENT_SECRET;

// Basic 인증을 위한 base64 인코딩된 credentials
const credentials = btoa(`${clientId}:${clientSecret}`);

const githubHeaders = {
  Authorization: `Basic ${credentials}`,
  Accept: "application/vnd.github.v3+json",
  "X-GitHub-Api-Version": "2022-11-28",
};

export const getGithubUserReposAPI = async (githubId: string) => {
  const res = await fetch(`${githubUrl}/users/${githubId}/repos`, {
    method: "GET",
    headers: githubHeaders,
  });

  if (!res.ok) {
    throw new Error(`GitHub API Error: ${res.status}`);
  }

  const data = await res.json();
  return data;
};

export const getGithubUserOrgsAPI = async (githubId: string) => {
  const res = await fetch(`${githubUrl}/users/${githubId}/orgs`, {
    method: "GET",
    headers: githubHeaders,
  });

  if (!res.ok) {
    throw new Error(`GitHub API Error: ${res.status}`);
  }

  const data = await res.json();
  return data;
};

export const getGithubOrgReposAPI = async (organization: string) => {
  const res = await fetch(`${githubUrl}/orgs/${organization}/repos`, {
    method: "GET",
    headers: githubHeaders,
  });

  if (!res.ok) {
    throw new Error(`GitHub API Error: ${res.status}`);
  }

  const data = await res.json();
  return data;
};

export const getGithubCollaboratorsAPI = async (
  organization: string,
  title: string,
) => {
  const res = await fetch(
    `${githubUrl}/repos/${organization}/${title}/contributors`,
    {
      method: "GET",
      headers: githubHeaders,
    },
  );

  if (!res.ok) {
    throw new Error(`GitHub API Error: ${res.status}`);
  }

  const data = await res.json();
  return data;
};
