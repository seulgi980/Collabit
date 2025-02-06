import { ChatRoomDetailResponse } from "@/shared/types/response/chat";
import { create } from "zustand";
import { devtools } from "zustand/middleware";

interface ChatRoomState {
  id: number | null;
  chatRoomDetail: ChatRoomDetailResponse | null;
}

interface ChatRoomActions {
  setId: (id: number) => void;
  setChatRoomDetail: (chatRoomDetail: ChatRoomDetailResponse) => void;
}

export const useChatStore = create<ChatRoomState & ChatRoomActions>()(
  devtools(
    (set) => ({
      id: null,
      chatRoomDetail: null,
      setId: (id) => set({ id }),
      setChatRoomDetail: (chatRoomDetail) => set({ chatRoomDetail }),
    }),
    {
      name: "Chat Store",
    },
  ),
);
