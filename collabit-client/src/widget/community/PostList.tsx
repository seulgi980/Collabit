import CommunityCard from "@/features/community/ui/CommunityCard";
import { getPostListAPI } from "@/shared/api/community";
import { PostListResponse } from "@/shared/types/response/post";
import { useInfiniteQuery } from "@tanstack/react-query";
import { useRef } from "react";
interface PostListProps {
  initialPosts: PostListResponse[];
}
const PostList = ({ initialPosts }: PostListProps) => {
  const ref = useRef<HTMLDivElement>(null);

  const { data, fetchNextPage, hasNextPage, isFetchingNextPage } =
    useInfiniteQuery({
      queryKey: ["posts"],
      queryFn: async ({ pageParam = 1 }) => {
        const response = await getPostListAPI({ currentPage: pageParam });
        return response;
      },
      initialData: {
        pages: [initialPosts],
        pageParams: [1],
      },
      getNextPageParam: (lastPage) => {
        return lastPage.hasNext ? lastPage.pageNumber + 1 : undefined;
      },
    });

  return (
    <>
      {data?.pages.map((group, i) => (
        <div key={i}>
          {group.map((post) => (
            <CommunityCard key={post.code} post={post} />
          ))}
        </div>
      ))}

      {/* 로딩 인디케이터 및 관찰 대상 요소 */}
      <div ref={ref} className="h-10">
        {isFetchingNextPage && (
          <div className="flex justify-center">
            <span>로딩 중...</span>
          </div>
        )}
      </div>
    </>
  );
};

export default PostList;
