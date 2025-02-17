"use client";

import CompareScoreSection from "@/features/main/CompareScoreSection";
import MyProjectSection from "@/features/main/MyProjectSection";
import { useState } from "react";
import { useEffect } from "react";
import PostCarouselSection from "@/features/community/ui/PostCarouselSection";
import useModalStore from "@/shared/lib/stores/modalStore";
import NotificationModal from "@/widget/ui/modals/NotificationModal";

export default function Home() {
  const [isMounted, setIsMounted] = useState(false);
  const { openModal, closeModal } = useModalStore();

  useEffect(() => {
    setIsMounted(true);

    const notificationCount = Number(
      localStorage.getItem("notificationCount") || "0",
    );

    if (notificationCount < 5) {
      openModal(
        <NotificationModal
          title="알림"
          description={`안녕하세요, Collabit 팀입니다.\n
    Collabit은 함께 프로젝트를 진행했던 동료들로부터 \n나의 협업능력에 대해 피드백을 받는 플랫폼입니다. \n
    "Github로그인 > 프로젝트 등록 > URL 공유 > 피드백 > 리포트 생성"\n 순으로 진행하시면 됩니다. \n
    생성된 리포트는 URL 공유, PDF 생성 등 다양하게 활용할 수 있습니다.

    이용관련 피드백은 커뮤니티에 자유롭게 올려주시면 \n반영하여 업데이트를 진행하도록 하겠습니다.
    감사합니다!😊`}
          buttonText="확인"
          handleButtonClick={() => {
            localStorage.setItem(
              "notificationCount",
              String(notificationCount + 1),
            );
            closeModal();
          }}
        />,
      );
    }
  }, [openModal, closeModal]);

  if (!isMounted) return null;

  return (
    <div className="m-auto flex max-w-5xl flex-col items-center gap-11 py-5 md:py-10">
      <h2 className="sr-only">
        메인페이지, 사용자 평균 협업 점수와 프로젝트 소식과 요즘 핫한 소식을
        확인하세요.
      </h2>
      <CompareScoreSection />
      <MyProjectSection />
      <PostCarouselSection type="latest" />
    </div>
  );
}
