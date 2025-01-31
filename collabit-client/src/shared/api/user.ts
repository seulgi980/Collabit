import { UserInfoResponse } from "../types/response/user";

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
const apiUrl = process.env.NEXT_PUBLIC_API_URL;

export const getUserInfoAPI = async (): Promise<UserInfoResponse> => {
  try {
    const res = await fetch(`${apiUrl}/user`, {
      credentials: "include",
    });

    if (!res.ok) {
      return { userInfo: undefined, isAuthencicated: false };
    }
    const data = await res.json();
    return { userInfo: data, isAuthencicated: true };
  } catch (error) {
    console.error(error);
    return { userInfo: undefined, isAuthencicated: false };
  }
};

export const updateUserNicknameAPI = async (
  body: UpdateUserNicknameRequest,
) => {
  const res = await fetch(`${apiUrl}/user/nickname`, {
    method: "PATCH",
    body: JSON.stringify(body),
  });
  const data = await res.json();
  return data;
};

// ???? 뭐하는 api인지 모르겠음
export const checkUserPasswordAPI = async (body: CheckUserPasswordRequest) => {
  const res = await fetch(`${apiUrl}/user/password`, {
    method: "POST",
    body: JSON.stringify(body),
  });
  const data = await res.json();
  return data;
};

export const updateUserPasswordAPI = async (
  body: UpdateUserPasswordRequest,
) => {
  const res = await fetch(`${apiUrl}/user/password`, {
    method: "PATCH",
    body: JSON.stringify(body),
  });
  const data = await res.json();
  return data;
};

export const changeUserImageAPI = async (body: ChangeUserImageRequest) => {
  const res = await fetch(`${apiUrl}/user/image`, {
    method: "PATCH",
    body: JSON.stringify(body),
  });
  const data = await res.json();
  return data;
};

export const deleteUserAPI = async () => {
  const res = await fetch(`${apiUrl}/user`, {
    method: "DELETE",
  });
  const data = await res.json();
  return data;
};

export const getUserLikePostsAPI = async () => {
  const res = await fetch(`${apiUrl}/user/like`);
  const data = await res.json();
  return data;
};

export const getUserPostsAPI = async () => {
  const res = await fetch(`${apiUrl}/user/post`);
  const data = await res.json();
  return data;
};

export const getUserCommentsAPI = async () => {
  const res = await fetch(`${apiUrl}/user/comment`);
  const data = await res.json();
  return data;
};
