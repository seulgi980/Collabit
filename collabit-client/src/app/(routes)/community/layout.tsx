"use client";

import { usePathname } from "next/navigation";

const CommunityLayout = ({
  list,
  post,
}: {
  list: React.ReactNode;
  post: React.ReactNode;
}) => {
  const pathname = usePathname();
  const isPostPage = pathname.includes("/community/");

  return (
    <>
      {/* 모바일 레이아웃 */}
      <div className="md:hidden">{isPostPage ? post : list}</div>

      {/* 데스크톱 레이아웃 */}
      <div className="mx-auto hidden w-full max-w-5xl items-center py-5 md:flex md:flex-col md:py-10">
        {post}
        {isPostPage ? null : list}
      </div>
    </>
  );
};

export default CommunityLayout;
