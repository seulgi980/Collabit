"use client";
import { useToast } from "@/shared/hooks/use-toast";
import { Button } from "@/shared/ui/button";
import React from "react";

import ReportHeader from "@/entities/report/ui/ReportHeader";
import { useAuth } from "@/features/auth/api/useAuth";
import AISummarySection from "@/features/report/ui/AISummarySection";
import CloudSection from "@/features/report/ui/CloudSection";
import ScoreAnalysisSection from "@/features/report/ui/ScoreAnalysisSection";

export default function Page() {
  const { toast } = useToast();
  const [reportData, setReportData] = React.useState(mockData);
  const [loading, setLoading] = React.useState(false);
  const { userInfo } = useAuth();
  const handleGenerateReport = async () => {
    setLoading(true);
    try {
      toast({
        title: "리포트가 생성되었습니다!",
        description: "최신 결과를 확인하세요.",
      });
    } catch {
      toast({ title: "오류 발생", description: "리포트 생성에 실패했습니다." });
    } finally {
      setLoading(false);
    }
  };

  if (!reportData) {
    return (
      <div className="mt-6 text-center">
        <p className="text-gray-600">
          아직 리포트를 생성한 적이 없습니다. 리포트를 생성해보세요!
        </p>
        <Button onClick={handleGenerateReport}>리포트 생성하기</Button>
      </div>
    );
  }
  return (
    <div className="mx-auto mt-5 w-full p-4 md:max-w-5xl">
      <ReportHeader />
      <div className="flex flex-col gap-10 py-4">
        <ScoreAnalysisSection
          userInfo={userInfo!}
          chartScore={mockData.skills}
          highestSkill={mockData.highestSkill}
          lowestSkill={mockData.lowestSkill}
        />
        <CloudSection
          positive={mockData.wordCloud.positive}
          negative={mockData.wordCloud.negative}
        />

        <AISummarySection
          positive={mockData.AISummary.positive}
          negative={mockData.AISummary.negative}
        />

        {/* <CommunityCard post={mockData[0]} /> */}
      </div>
    </div>
  );
}

const mockData = {
  skills: {
    "공감(S)": 85,
    "경청(A)": 80,
    "표현(E)": 75,
    "문제해결(PS)": 90,
    "갈등해결(CS)": 70,
    "리더십(L)": 60,
  },
  highestSkill: {
    name: "문제해결(PS)",
    description:
      "문제해결력은 업무 수행 중 발생하는 다양한 문제 상황을 체계적으로 분석하고, 효율적인 해결방안을 도출하여 실행하는 능력을 평가하는 지표입니다.",
    aiComment: "상대방의 의견을 잘 듣고 이해하는 능력이 뛰어납니다.",
  },
  lowestSkill: {
    name: "리더십(L)",
    description:
      "리더십은 팀원들과의 효과적인 소통을 통해 공동의 목표를 달성하고, 팀원 개개인의 성장을 지원하며, 건설적인 피드백을 제공하는 능력을 평가하는 지표입니다.",
    aiComment:
      "팀원들과의 효과적인 소통을 통해 공동의 목표를 달성하고, 팀원 개개인의 성장을 지원하며, 건설적인 피드백을 제공하는 능력이 필요합니다.",
  },
  wordCloud: {
    positive: [
      { text: "협력", weight: 80 },
      { text: "소통", weight: 70 },
      { text: "신뢰", weight: 60 },
      { text: "팀워크", weight: 90 },
      { text: "적극성", weight: 50 },
      { text: "창의성", weight: 55 },
      { text: "유연성", weight: 40 },
      { text: "존중", weight: 100 },
      { text: "배려", weight: 85 },
      { text: "의사소통", weight: 75 },
      { text: "협업", weight: 95 },
      { text: "조화", weight: 50 },
      { text: "지원", weight: 60 },
      { text: "동기부여", weight: 65 },
    ],
    negative: [
      { text: "갈등", weight: 80 },
      { text: "오해", weight: 70 },
      { text: "불만", weight: 75 },
      { text: "소외", weight: 60 },
      { text: "부정적", weight: 65 },
      { text: "비협조", weight: 70 },
      { text: "무관심", weight: 50 },
      { text: "경쟁", weight: 75 },
      { text: "혼란", weight: 60 },
      { text: "소통 부족", weight: 80 },
      { text: "불신", weight: 70 },
      { text: "압박감", weight: 65 },
      { text: "갈등 상황", weight: 75 },
    ],
  },
  AISummary: {
    positive:
      '팀원들의 주관식 응답을 분석한 결과, 크게 세 가지 주요 의견이 도출되었습니다. 첫째, 의사소통 방식에 있어 정기적인 팀 미팅과 1:1 면담이 긍정적인 영향을 미쳤다는 의견이 다수였습니다. 특히 "어려운 이야기도 편하게 할 수 있는 분위기"가 형성되어 있다는 평가가 많았습니다. 둘째, 업무 프로세스 측면에서는 명확한 목표 설정과 체계적인 일정 관리가 팀의 강점으로 꼽혔습니다. 다만, 일부 팀원들은 업무 우선순위 설정에 대한 보다 구체적인 가이드라인이 필요하다고 제안했습니다. 마지막으로, 기술적 역량 강화를 위한 스터디 문화와 코드 리뷰 시스템이 잘 정착되어 있다는 평가가 있었으며, 이는 팀원들의 성장에 큰 도움이 되고 있다고 합니다.',
    negative:
      '팀원들의 주관식 응답을 분석한 결과, 크게 세 가지 주요 의견이 도출되었습니다. 첫째, 의사소통 방식에 있어 정기적인 팀 미팅과 1:1 면담이 긍정적인 영향을 미쳤다는 의견이 다수였습니다. 특히 "어려운 이야기도 편하게 할 수 있는 분위기"가 형성되어 있다는 평가가 많았습니다. 둘째, 업무 프로세스 측면에서는 명확한 목표 설정과 체계적인 일정 관리가 팀의 강점으로 꼽혔습니다. 다만, 일부 팀원들은 업무 우선순위 설정에 대한 보다 구체적인 가이드라인이 필요하다고 제안했습니다. 마지막으로, 기술적 역량 강화를 위한 스터디 문화와 코드 리뷰 시스템이 잘 정착되어 있다는 평가가 있었으며, 이는 팀원들의 성장에 큰 도움이 되고 있다고 합니다.',
  },
};
