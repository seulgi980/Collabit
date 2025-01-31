import {
  ProjectCreateRequest,
  ProjectRemoveRequest,
} from "../types/request/Project";

const apiUrl = process.env.NEXT_PUBLIC_API_URL;
const githubUrl = "https://api.github.com/";

export const getGithubUserReposAPI = async (githubId: string) => {
  const res = await fetch(`${githubUrl}/users/${githubId}/repos`, {
    method: "GET",
  });
  const data = await res.json();
  return data;
};

export const getGithubUserOrgsAPI = async (githubId: string) => {
  const res = await fetch(`${githubUrl}/users/${githubId}/orgs`, {
    method: "GET",
  });
  const data = await res.json();
  return data;
};

export const getGithubOrgReposAPI = async (organization: string) => {
  const res = await fetch(`${githubUrl}/orgs/${organization}/repos`, {
    method: "GET",
  });
  const data = await res.json();
  return data;
};

export const getGithubCollaboratorsAPI = async (
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

export const createProjectAPI = async (body: ProjectCreateRequest) => {
  const res = await fetch(`${apiUrl}/project`, {
    method: "POST",
    body: JSON.stringify(body),
  });
  const data = await res.json();
  return data;
};

export const getProjectListAPI = async () => {
  const res = await fetch(`${apiUrl}/project`, {
    method: "GET",
  });
  const data = await res.json();
  return data;
};

export const removeProjectAPI = async (body: ProjectRemoveRequest) => {
  const res = await fetch(`${apiUrl}/project`, {
    method: "DELETE",
    body: JSON.stringify(body),
  });
  const data = await res.json();
  return data;
};

export const searchProjectAPI = async (keyword: string) => {
  const res = await fetch(`${apiUrl}/project/search?keyword=${keyword}`, {
    method: "GET",
  });
  const data = await res.json();
  return data;
};

export const updateProjectDoneAPI = async (code: number) => {
  const res = await fetch(`${apiUrl}/project/done/${code}`, {
    method: "GET",
  });
  const data = await res.json();
  return data;
};

export const checkProjectUpdate = async () => {
  const res = await fetch(`${apiUrl}/project/new`, {
    method: "GET",
  });
  const data = await res.json();
  return data;
};

export const getAddedProject = async () => {
  const res = await fetch(`${apiUrl}/project/added`, { method: "GET" });
  const data = await res.json();
  return data;
};
