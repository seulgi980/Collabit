"use client";

import { useAuth } from "@/features/auth/api/useAuth";
import { Button } from "@/shared/ui/button";
import { Share } from "lucide-react";

const ReportHeader = () => {
  const { userInfo } = useAuth();
  const handleShare = () => {
    console.log("share");
  };
  return (
    <div className="flex justify-between border-b py-2">
      <div className="flex flex-col gap-2">
        <h1 className="text-2xl font-bold">
          {userInfo?.nickname}님의 협업 역량 리포트
        </h1>
        <div className="flex gap-2">
          <p className="text-sm">
            <span>참여인원 </span>
            <span className="font-semibold text-violet-500">15명</span>
          </p>
          <p className="text-sm">
            <span>프로젝트 </span>
            <span className="font-semibold text-violet-500">3회</span>
          </p>
        </div>
      </div>
      <Button variant={"outline"} onClick={handleShare}>
        <Share className="h-4 w-4" />
        리포트 공유
      </Button>
    </div>
  );
};

export default ReportHeader;
