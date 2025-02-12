"use client";

import React, { useRef, useState, useEffect } from "react";
import { useAuth } from "@/features/auth/api/useAuth";
import { Button } from "@/shared/ui/button";
import { Share, RefreshCw } from "lucide-react";
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/shared/ui/tooltip";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/shared/ui/dropdown-menu";
import { FileDown, Link2 } from "lucide-react";
import useReport from "@/features/report/api/useReport";
import hashUser from "@/shared/utils/hashUser";
import ReportPDF from "@/widget/report/ui/ReportPDF";

const ReportHeader = () => {
  const { userInfo } = useAuth();
  const { portfolioInfo } = useReport();
  const reportPDFRef = useRef<{ handleDownloadPDF: () => void } | null>(null);
  const [shareUrl, setShareUrl] = useState<string>("");

  useEffect(() => {
    const fetchHashedValue = async () => {
      if (userInfo?.githubId) {
        try {
          const hashedValue = await hashUser(userInfo.githubId);
          const shareUrl = `${process.env.NEXT_PUBLIC_SHARE_URL}/${hashedValue}`;
          setShareUrl(shareUrl);
        } catch (error) {
          console.error("Hashing failed:", error);
        }
      }
    };
    fetchHashedValue();
  }, [userInfo]);

  const handleDownloadPDF = () => {
    console.log("📄 PDF 다운로드 요청...");
    reportPDFRef.current?.handleDownloadPDF();
  };

  const handleCopyLink = () => {
    navigator.clipboard.writeText(shareUrl);
  };

  const handleRefresh = () => {
    console.log("refresh");
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
            <span className="font-semibold text-violet-500">
              {portfolioInfo?.participant}명
            </span>
          </p>
          <p className="text-sm">
            <span>프로젝트 </span>
            <span className="font-semibold text-violet-500">
              {portfolioInfo?.project}회
            </span>
          </p>
        </div>
      </div>
      <div className="flex gap-1.5">
        <TooltipProvider>
          <Tooltip>
            <TooltipTrigger asChild>
              <Button variant="ghost" size="icon" onClick={handleRefresh}>
                <RefreshCw className="h-4 w-4 text-gray-600" />
              </Button>
            </TooltipTrigger>
            <TooltipContent>
              <p>새로고침</p>
            </TooltipContent>
          </Tooltip>

          <DropdownMenu>
            <Tooltip>
              <TooltipTrigger asChild>
                <DropdownMenuTrigger asChild>
                  <Button variant="ghost" size="icon">
                    <Share className="h-4 w-4 text-gray-600" />
                  </Button>
                </DropdownMenuTrigger>
              </TooltipTrigger>
              <TooltipContent>
                <p>공유하기</p>
              </TooltipContent>
            </Tooltip>
            <DropdownMenuContent>
              <DropdownMenuItem onClick={handleDownloadPDF}>
                <FileDown className="mr-2 h-4 w-4" />
                PDF로 저장하기
              </DropdownMenuItem>
              <DropdownMenuItem onClick={handleCopyLink}>
                <Link2 className="mr-2 h-4 w-4" />
                링크 복사하기
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
        </TooltipProvider>
      </div>
      <ReportPDF ref={reportPDFRef} shareUrl={shareUrl} />
    </div>
  );
};

export default ReportHeader;
