import { loginGithubAPI } from "@/shared/api/auth";

export const fetchLoginGithub = async () => {
  try {
    const res = await loginGithubAPI();
    console.log(res);
    return res;
  } catch (error) {
    console.error(error);
  }
};
