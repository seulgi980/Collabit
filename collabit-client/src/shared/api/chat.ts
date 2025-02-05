import { ChatListResponse } from "../types/response/chat";

const apiUrl = process.env.NEXT_PUBLIC_API_URL;

export const getChatListAPI = async (): Promise<ChatListResponse[]> => {
  const response = await fetch(`${apiUrl}/chat/room/list`, {
    headers: { "Content-Type": "application/json" },
    credentials: "include",
  });
  if (!response.ok) {
    throw new Error("채팅 리스트 조회 실패");
  }
  return response.json();
};
