interface SignupRequest {
  email: string;
  password: string;
  nickname: string;
}

interface LoginRequest {
  email: string;
  password: string;
}

const apiUrl = process.env.NEXT_PUBLIC_API_URL;

export const sendEmailCodeAPI = async (email: string) => {
  const res = await fetch(`${apiUrl}/auth/send-email`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ email }),
  });
  console.log(res);

  const data = await res.json();

  return data;
};

export const validateEmailCodeAPI = async ({
  email,
  code,
}: {
  email: string;
  code: string;
}) => {
  const res = await fetch(`${apiUrl}/auth/email-check`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ email, code }),
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
    credentials: "include",
  });
  return res;
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
