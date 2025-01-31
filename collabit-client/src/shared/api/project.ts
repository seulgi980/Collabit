import {
  ProjectCreateRequest,
  ProjectRemoveRequest,
} from "../types/request/Project";

const apiUrl = process.env.NEXT_PUBLIC_API_URL;
const githubUrl = "https://api.github.com/";

export const githubUserReposAPI = async (githubId: string) => {
  const res = await fetch(`${githubUrl}/users/${githubId}/repos`, {
    method: "GET",
  });
  const data = await res.json();
  return data;
};

export const githubUserOrgsAPI = async (githubId: string) => {
  const res = await fetch(`${githubUrl}/users/${githubId}/orgs`, {
    method: "GET",
  });
  const data = await res.json();
  return data;
};

export const githubOrgReposAPI = async (organization: string) => {
  const res = await fetch(`${githubUrl}/orgs/${organization}/repos`, {
    method: "GET",
  });
  const data = await res.json();
  return data;
};

export const githubCollaboratorsAPI = async (
  organization: string,
  title: string,
) => {
  const res = await fetch(
    `${githubUrl}/repos/${organization}/${title}/collaborators`,
    {
      method: "GET",
    },
  );
  const data = await res.json();
  return data;
};

export const projectCreateAPI = async (body: ProjectCreateRequest) => {
  const res = await fetch(`${apiUrl}/project`, {
    method: "POST",
    body: JSON.stringify(body),
  });
  const data = await res.json();
  return data;
};

export const projectListAPI = async () => {
  const res = await fetch(`${apiUrl}/project`, {
    method: "GET",
  });
  const data = await res.json();
  return data;
};

export const projectRemoveAPI = async (body: ProjectRemoveRequest) => {
  const res = await fetch(`${apiUrl}/project`, {
    method: "DELETE",
    body: JSON.stringify(body),
  });
  const data = await res.json();
  return data;
};

export const projectSearchAPI = async (keyword: string) => {
  const res = await fetch(`${apiUrl}/project/search?keyword=${keyword}`, {
    method: "GET",
  });
  const data = await res.json();
  return data;
};

export const projectDoneAPI = async (code: number) => {
  const res = await fetch(`${apiUrl}/project/done/${code}`, {
    method: "GET",
  });
  const data = await res.json();
  return data;
};

export const projectUpdateCheck = async () => {
  const res = await fetch(`${apiUrl}/project/new`, {
    method: "GET",
  });
  const data = await res.json();
  return data;
};
