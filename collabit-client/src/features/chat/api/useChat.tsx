import { getChatMessagesAPI, getChatRoomDetailAPI } from "@/shared/api/chat";
import { useInfiniteQuery, useQuery } from "@tanstack/react-query";

export const useChat = (roomCode?: number) => {
  // ✅ 채팅방 정보 조회 (5분 동안 캐시 유지)
  const {
    data: chatRoom,
    isLoading: chatRoomLoading,
    error: chatRoomError,
  } = useQuery({
    queryKey: ["chatRoom", roomCode],
    queryFn: () => (roomCode ? getChatRoomDetailAPI(roomCode) : null),
    enabled: !!roomCode,
    staleTime: 1000 * 60 * 5,
  });

  // ✅ 채팅 메시지 조회 (페이징)
  const { data, fetchNextPage, hasNextPage, isLoading, error } =
    useInfiniteQuery({
      queryKey: ["chatMessages", roomCode],
      queryFn: async ({ pageParam = 1 }) => {
        if (!roomCode)
          return { content: [], hasNext: false, pageNumber: pageParam };

        const response = await getChatMessagesAPI(roomCode, pageParam);

        if (!response || response.content.length === 0) {
          return { content: [], hasNext: false, pageNumber: pageParam };
        }

        return response;
      },
      getNextPageParam: (lastPage) =>
        lastPage.hasNext ? lastPage.pageNumber + 1 : undefined,
      initialPageParam: 1,
      enabled: !!roomCode,
    });

  // ✅ 메시지 리스트 평탄화
  const messages = data?.pages.flatMap((page) => page.content) ?? [];

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
