import {
  ChatMessageResponse,
  ChatRoomDetailResponse,
  ChatRoomListResponse,
} from "@/shared/types/response/chat";
import { WebSocketMessage } from "@/shared/types/model/Chat";
import { create } from "zustand";
import { devtools } from "zustand/middleware";
import { getUnreadMessagesAPI, getChatRoomListAPI } from "@/shared/api/chat";

interface ChatState {
  chatMessages: ChatMessageResponse[];
  chatId: number | null;
  chatRoomDetail: ChatRoomDetailResponse | null;
  chatRooms: ChatRoomListResponse[];
  userStatuses: Record<string, boolean>;
  unreadCounts: Record<number, number>;
  sendMessage: ((message: WebSocketMessage) => void) | null;
}

interface ChatActions {
  setChatId: (chatId: number | null) => void;
  setChatRoomDetail: (chatRoomDetail: ChatRoomDetailResponse) => void;
  setChatMessages: (messages: WebSocketMessage[]) => void;
  addMessage: (message: WebSocketMessage) => void;
  setSendMessage: (
    sendMessage: ((message: WebSocketMessage) => void) | null,
  ) => void;
  updateChatMessages: (messages: ChatMessageResponse[]) => void;
  setChatRooms: (rooms: ChatRoomListResponse[]) => void;
  updateUserStatus: (userCode: string, isOnline: boolean) => void;
  updateUnreadCount: (roomCode: number, count: number) => void;
  fetchChatRooms: (pageNumber: number) => Promise<void>;
  fetchUnreadCounts: () => Promise<void>;
}

export const useChatStore = create<ChatState & ChatActions>()(
  devtools((set) => ({
    chatMessages: [],
    chatId: null,
    chatRoomDetail: null,
    chatRooms: [],
    userStatuses: {},
    unreadCounts: {},
    sendMessage: null,

    setChatMessages: (messages: ChatMessageResponse[]) =>
      set({ chatMessages: messages }),

    addMessage: (message: ChatMessageResponse) =>
      set((state) => ({
        chatMessages: [...state.chatMessages, message],
      })),

    setChatId: (chatId) => set({ chatId }),
    setChatRoomDetail: (chatRoomDetail) => set({ chatRoomDetail }),
    setSendMessage: (sendMessage) => set({ sendMessage }),
    setChatRooms: (rooms) => set({ chatRooms: rooms }),
    updateChatMessages: (messages: ChatMessageResponse[]) =>
      set({ chatMessages: messages }),
    updateUserStatus: (userCode, isOnline) =>
      set((state) => ({
        userStatuses: { ...state.userStatuses, [userCode]: isOnline },
      })),

    updateUnreadCount: (roomCode, count) =>
      set((state) => ({
        unreadCounts: {
          ...state.unreadCounts,
          [roomCode]: (state.unreadCounts[roomCode] || 0) + count,
        },
      })),

    fetchChatRooms: async (pageNumber) => {
      try {
        const response = await getChatRoomListAPI(pageNumber);
        set({ chatRooms: response.content });
      } catch (error) {
        console.error("채팅방 목록 조회 실패:", error);
      }
    },

    fetchUnreadCounts: async () => {
      try {
        const response = await getUnreadMessagesAPI();
        set({ unreadCounts: response });
      } catch (error) {
        console.error("안 읽은 메시지 조회 실패:", error);
      }
    },
  })),
);