import TabNavButton from "@/entities/common/ui/TabNavButton";

export default function Layout({ children }: { children: React.ReactNode }) {
  return (
    <div className="flex flex-col items-center md:py-4">
      <div className="mx-auto flex w-[240px] justify-evenly py-3 md:w-[360px]">
        <TabNavButton href="/mypage/profile">회원정보</TabNavButton>
        <TabNavButton href="/mypage/contents">나의 게시글</TabNavButton>
      </div>
      {children}
    </div>
  );
}
