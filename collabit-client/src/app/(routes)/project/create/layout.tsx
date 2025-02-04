"use client";
import PageHeader from "@/entities/common/ui/PageHeader";
import useMediaQuery from "@/shared/hooks/useMediaQuery";
import { useRouter, useSearchParams } from "next/navigation";

export default function Layout({
  list,
  detail,
}: {
  list: React.ReactNode;
  detail: React.ReactNode;
}) {
  const searchParams = useSearchParams();
  const router = useRouter();
  const isMobile = useMediaQuery("(max-width: 768px)");
  const isDetailView = searchParams.size > 0;

  return (
    <div className="mx-auto flex w-full max-w-5xl flex-col justify-center gap-5 py-10">
      <PageHeader
        mainTitle="프로젝트 등록"
        subTitle="프로젝트를 등록하고, 동료들에게 피드백을 요청해보세요."
        handleBack={
          isDetailView && isMobile
            ? () => router.push("/project/create")
            : undefined
        }
      />

      {/* 모바일 레이아웃 */}
      <div className="md:hidden">{isDetailView ? detail : list}</div>

      {/* 데스크톱 레이아웃 */}
      <div className="hidden w-full gap-4 md:flex">
        <div className="w-1/2">{list}</div>
        <div className="w-1/2">{detail}</div>
      </div>
    </div>
  );
}
