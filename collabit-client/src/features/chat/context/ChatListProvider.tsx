import { ChatListResponse } from "@/shared/types/response/chat";
import {
  createContext,
  Dispatch,
  SetStateAction,
  useContext,
  useState,
} from "react";

export const ChatListContext = createContext<{
  chatList: ChatListResponse[];
  setChatList: Dispatch<SetStateAction<ChatListResponse[]>>;
} | null>(null);

export const ChatListProvider = ({
  children,
  initialData,
}: {
  children: React.ReactNode;
  initialData: ChatListResponse[];
}) => {
  const [chatList, setChatList] = useState(initialData);
  return (
    <ChatListContext.Provider value={{ chatList, setChatList }}>
      {children}
    </ChatListContext.Provider>
  );
};

export const useChatList = () => {
  const context = useContext(ChatListContext);
  if (!context) {
    throw new Error("useChatList must be used within a ChatListProvider");
  }
  return context;
};
