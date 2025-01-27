import { ENV } from "../config/env";

// api 명세 수정중
interface UpdateUserNicknameRequest {
  nickname: string;
}

// api 명세 수정중
interface CheckUserPasswordRequest {
  password: string;
}

// api 명세 수정중
interface UpdateUserPasswordRequest {
  password: string;
}

// api 명세 수정중
interface ChangeUserImageRequest {
  image: string;
}

export const getUserInfoAPI = async () => {
  const res = await fetch(`${ENV.API_URL}/user`);
  const data = await res.json();
  return data;
};

export const updateUserNicknameAPI = async (
  body: UpdateUserNicknameRequest,
) => {
  const res = await fetch(`${ENV.API_URL}/user/nickname`, {
    method: "PATCH",
    body: JSON.stringify(body),
  });
  const data = await res.json();
  return data;
};

// ???? 뭐하는 api인지 모르겠음
export const checkUserPasswordAPI = async (body: CheckUserPasswordRequest) => {
  const res = await fetch(`${ENV.API_URL}/user/password`, {
    method: "POST",
    body: JSON.stringify(body),
  });
  const data = await res.json();
  return data;
};

export const updateUserPasswordAPI = async (
  body: UpdateUserPasswordRequest,
) => {
  const res = await fetch(`${ENV.API_URL}/user/password`, {
    method: "PATCH",
    body: JSON.stringify(body),
  });
  const data = await res.json();
  return data;
};

export const changeUserImageAPI = async (body: ChangeUserImageRequest) => {
  const res = await fetch(`${ENV.API_URL}/user/image`, {
    method: "PATCH",
    body: JSON.stringify(body),
  });
  const data = await res.json();
  return data;
};

export const deleteUserAPI = async () => {
  const res = await fetch(`${ENV.API_URL}/user`, {
    method: "DELETE",
  });
  const data = await res.json();
  return data;
};

export const getUserLikePostsAPI = async () => {
  const res = await fetch(`${ENV.API_URL}/user/like`);
  const data = await res.json();
  return data;
};

export const getUserPostsAPI = async () => {
  const res = await fetch(`${ENV.API_URL}/user/post`);
  const data = await res.json();
  return data;
};

export const getUserCommentsAPI = async () => {
  const res = await fetch(`${ENV.API_URL}/user/comment`);
  const data = await res.json();
  return data;
};
