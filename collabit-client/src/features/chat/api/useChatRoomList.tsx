import { useAuth } from "@/features/auth/api/useAuth";
import { getChatRoomListAPI } from "@/shared/api/chat";
import { useInfiniteQuery } from "@tanstack/react-query";
import { useEffect } from "react";
import { useChatStore } from "@/shared/lib/stores/chatStore";

export const useChatRoomList = () => {
  const { userInfo } = useAuth();
  const { chatMessages } = useChatStore();

  const {
    data,
    error,
    fetchNextPage,
    hasNextPage,
    isFetching,
    isFetchingNextPage,
    status,
    refetch,
  } = useInfiniteQuery({
    queryKey: ["chatList", userInfo?.nickname],
    queryFn: async ({ pageParam = 0 }) => {
      const response = await getChatRoomListAPI(pageParam);
      if (!response || response.content.length === 0) {
        return { content: [], hasNext: false, pageNumber: pageParam };
      }
      return response;
    },
    getNextPageParam: (lastPage) =>
      !lastPage.hasNext ? undefined : lastPage.pageNumber + 1,
    initialPageParam: 0,
    enabled: !!userInfo?.nickname,
  });

  // messages가 변경될 때마다 chatRoomList를 다시 가져옴
  useEffect(() => {
    if (chatMessages.length > 0) {
      refetch();
    }
  }, [chatMessages, refetch]);

  const chatList = data?.pages.flatMap((page) => page.content);

  return {
    chatList,
    error,
    status,
    hasNextPage,
    isFetching,
    isFetchingNextPage,
    fetchNextPage,
    refetch,
  };
};
