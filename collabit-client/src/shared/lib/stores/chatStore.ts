import {
  ChatMessageResponse,
  ChatRoomDetailResponse,
} from "@/shared/types/response/chat";
import { WebSocketMessage } from "@/shared/types/model/Chat";
import { create } from "zustand";
import { devtools } from "zustand/middleware";

type ChatMessagesUpdate =
  | ChatMessageResponse
  | ChatMessageResponse[]
  | ((prevMessages: ChatMessageResponse[]) => ChatMessageResponse[]);

interface ChatRoomState {
  chatId: number | null;
  chatRoomDetail: ChatRoomDetailResponse | null;
  chatMessages: ChatMessageResponse[];
  sendMessage: ((message: WebSocketMessage) => void) | null;
}

interface ChatRoomActions {
  setChatId: (chatId: number | null) => void;
  setChatRoomDetail: (chatRoomDetail: ChatRoomDetailResponse) => void;
  setChatMessages: (messages: ChatMessageResponse[]) => void;
  updateChatMessages: (update: ChatMessagesUpdate) => void;
  setSendMessage: (
    sendMessage: ((message: WebSocketMessage) => void) | null
  ) => void;
}

export const useChatStore = create<ChatRoomState & ChatRoomActions>()(
  devtools(
    (set) => ({
      chatId: null,
      chatRoomDetail: null,
      chatMessages: [],
      sendMessage: null,

      setChatId: (chatId: number | null) => set({ chatId }),
      setChatRoomDetail: (chatRoomDetail: ChatRoomDetailResponse) =>
        set({ chatRoomDetail }),

      setChatMessages: (messages: ChatMessageResponse[]) =>
        set({ chatMessages: messages }),

      updateChatMessages: (update: ChatMessagesUpdate) =>
        set((state) => {
          if (typeof update === "function") {
            return { chatMessages: update(state.chatMessages) };
          }
          if (Array.isArray(update)) {
            return { chatMessages: [...state.chatMessages, ...update] };
          }
          return { chatMessages: [...state.chatMessages, update] };
        }),

      setSendMessage: (sendMessage) => set({ sendMessage }),
    }),
    {
      name: "Chat Store",
    }
  )
);
