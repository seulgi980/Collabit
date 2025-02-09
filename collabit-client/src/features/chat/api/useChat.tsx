import { useAuth } from "@/features/auth/api/useAuth";
import { getChatMessagesAPI, getChatRoomDetailAPI } from "@/shared/api/chat";
import { useChatStore } from "@/shared/lib/stores/chatStore";
import { WebSocketMessage } from "@/shared/types/model/Chat";
import { useInfiniteQuery, useQuery } from "@tanstack/react-query";
import { useEffect } from "react";

export const useChat = () => {
  const { userInfo } = useAuth();
  const {
    chatId,
    chatRoomDetail,
    setChatRoomDetail,
    setChatMessages,
    addMessage,
  } = useChatStore();

  // 채팅방 디테일 쿼리
  const {
    data: chatRoom,
    isLoading: chatRoomLoading,
    error: chatRoomError,
  } = useQuery({
    queryKey: ["chatRoom", chatId],
    queryFn: () => getChatRoomDetailAPI(chatId!),
    enabled: !!userInfo?.nickname && !!chatId,
    staleTime: 1000 * 60 * 5, //5분 동안 유지
  });

  // 채팅 메시지 쿼리
  const {
    data: chatMessages,
    fetchNextPage,
    hasNextPage,
    isLoading,
    error,
  } = useInfiniteQuery({
    queryKey: ["chatMessages", chatId],
    queryFn: async ({ pageParam = 0 }) => {
      const response = await getChatMessagesAPI(chatId!, pageParam);
      if (!response || response.content.length === 0) {
        return { content: [], hasNext: false, pageNumber: pageParam };
      }
      return response;
    },
    getNextPageParam: (lastPage) =>
      lastPage.hasNext ? lastPage.pageNumber + 1 : undefined,
    initialPageParam: 0,
    enabled: !!userInfo?.nickname && !!chatId,
  });

  // 채팅방 디테일 저장
  useEffect(() => {
    if (chatRoom && chatMessages && chatRoomDetail !== chatRoom) {
      setChatRoomDetail(chatRoom);
      const messages =
        chatMessages?.pages.flatMap((page) => page.content) ?? [];
      setChatMessages(messages);
    }
  }, [
    chatRoom,
    setChatRoomDetail,
    chatRoomDetail,
    chatMessages,
    setChatMessages,
  ]);

  const handleNewMessage = (message: WebSocketMessage) => {
    addMessage(message);
  };

  return {
    data: chatMessages,
    chatRoomLoading,
    chatRoomError,
    isLoading,
    error,
    fetchNextPage,
    hasNextPage,
    handleNewMessage,
  };
};
