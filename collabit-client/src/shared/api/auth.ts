import { ENV } from "../config/env";

interface SignupRequest {
  email: string;
  password: string;
  nickname: string;
}

interface LoginRequest {
  email: string;
  password: string;
}

interface ValidateEmailRequest {
  email: string;
}

export const validateEmailAPI = async (body: ValidateEmailRequest) => {
  const res = await fetch(`${ENV.API_URL}/auth/email-check`, {
    method: "POST",
    body: JSON.stringify(body),
  });
  const data = await res.json();
  return data;
};

export const signupAPI = async (body: SignupRequest) => {
  const res = await fetch(`${ENV.API_URL}/auth/signup`, {
    method: "POST",
    body: JSON.stringify(body),
  });
  const data = await res.json();
  return data;
};

export const logoutAPI = async () => {
  const res = await fetch(`${ENV.API_URL}/auth/logout`, {
    method: "POST",
  });
  const data = await res.json();
  return data;
};

export const loginCredentialAPI = async (body: LoginRequest) => {
  const res = await fetch(`${ENV.API_URL}/auth/login`, {
    method: "POST",
    body: JSON.stringify(body),
  });
  const data = await res.json();
  return data;
};

export const loginGithubAPI = async () => {
  const res = await fetch(`${ENV.API_URL}/auth/oauth`);
  const data = await res.json();
  return data;
};

export const linkGithubAccountAPI = async () => {
  const res = await fetch(`${ENV.API_URL}/auth/oauth/link`);
  const data = await res.json();
  return data;
};
