"use client";
import PageHeader from "@/entities/common/ui/PageHeader";
import { usePathname } from "next/navigation";

export default function Layout(props: {
  list: React.ReactNode;
  detail: React.ReactNode;
}) {
  const pathname = usePathname();
  const isDetailView = pathname !== "/project/create";

  return (
    <div className="mx-auto flex w-full max-w-5xl flex-col justify-center gap-5 py-10">
      <PageHeader
        mainTitle="프로젝트 등록"
        subTitle="프로젝트를 등록하고, 동료들에게 피드백을 요청해보세요."
      />

      {/* 모바일 레이아웃 */}
      <div className="md:hidden">
        {isDetailView ? props.detail : props.list}
      </div>

      {/* 데스크톱 레이아웃 */}
      <div className="hidden w-full gap-4 md:flex">
        <div className="w-1/2">{props.list}</div>
        <div className="w-1/2">{props.detail}</div>
      </div>
    </div>
  );
}
