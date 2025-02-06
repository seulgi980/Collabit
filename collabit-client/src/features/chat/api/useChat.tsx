import { useAuth } from "@/features/auth/api/useAuth";
import { getChatMessagesAPI, getChatRoomDetailAPI } from "@/shared/api/chat";
import { useChatStore } from "@/shared/lib/stores/chatStore";
import { useInfiniteQuery, useQuery } from "@tanstack/react-query";
import { useEffect } from "react";

const { userInfo } = useAuth();

export const useChat = () => {
  const id = useChatStore((state) => state.id);
  const setChatRoomDetail = useChatStore((state) => state.setChatRoomDetail);

  // 채팅방 디테일 쿼리
  const {
    data: chatRoom,
    isLoading: chatRoomLoading,
    error: chatRoomError,
  } = useQuery({
    queryKey: ["chatRoom", userInfo?.nickname, id],
    queryFn: () => getChatRoomDetailAPI(id!),
    enabled: !!userInfo?.nickname && !!id,
    staleTime: 1000 * 60 * 5, //5분 동안 유지
  });

  // 채팅 메시지 쿼리
  const { data, fetchNextPage, hasNextPage, isLoading, error } =
    useInfiniteQuery({
      queryKey: ["chatMessages", userInfo?.nickname, id],
      queryFn: async ({ pageParam = 0 }) => {
        const response = await getChatMessagesAPI(id!, pageParam);
        if (!response || response.content.length === 0) {
          return { content: [], hasNext: false, pageNumber: pageParam };
        }
        return response;
      },
      getNextPageParam: (lastPage) =>
        lastPage.hasNext ? lastPage.pageNumber + 1 : undefined,
      initialPageParam: 0,
      enabled: !!userInfo?.nickname && !!id,
    });

  // 메시지 리스트 평탄화
  const messages = data?.pages.flatMap((page) => page.content) ?? [];

  useEffect(() => {
    if (chatRoom) {
      setChatRoomDetail(chatRoom);
    }
  }, [chatRoom, setChatRoomDetail, id]);

  return {
    chatRoom,
    messages,
    chatRoomLoading,
    chatRoomError,
    isLoading,
    error,
    fetchNextPage,
    hasNextPage,
  };
};
