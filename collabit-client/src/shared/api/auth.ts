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

const apiUrl = process.env.NEXT_PUBLIC_API_URL;

export const validateEmailAPI = async (body: ValidateEmailRequest) => {
  const res = await fetch(`${apiUrl}/auth/email-check`, {
    method: "POST",
    body: JSON.stringify(body),
  });
  const data = await res.json();
  return data;
};

export const signupAPI = async (body: SignupRequest) => {
  const res = await fetch(`${apiUrl}/auth/signup`, {
    method: "POST",
    body: JSON.stringify(body),
  });
  const data = await res.json();
  return data;
};

export const logoutAPI = async () => {
  const res = await fetch(`${apiUrl}/auth/logout`, {
    method: "POST",
  });
  const data = await res.json();
  return data;
};

export const loginCredentialAPI = async (body: LoginRequest) => {
  const res = await fetch(`${apiUrl}/auth/login`, {
    method: "POST",
    body: JSON.stringify(body),
  });
  const data = await res.json();
  return data;
};

export const linkGithubAccountAPI = async () => {
  const res = await fetch(`${apiUrl}/auth/oauth/link`);
  const data = await res.json();
  return data;
};
