"use client";
import { useToast } from "@/shared/hooks/use-toast";
import { useState } from "react";

import NoReport from "@/entities/report/ui/NoReport";
import ReportHeader from "@/entities/report/ui/ReportHeader";
import { useAuth } from "@/features/auth/api/useAuth";
import AISummarySection from "@/features/report/ui/AISummarySection";
import CloudSection from "@/features/report/ui/CloudSection";
import CompareSection from "@/features/report/ui/CompareSection";
import ScoreAnalysisSection from "@/widget/report/ui/ScoreAnalysisSection";
import useModalStore from "@/shared/lib/stores/modalStore";
import Image from "next/image";
import HistoryRateSection from "@/features/report/ui/HistoryRateSection";

export default function Page() {
  const { toast } = useToast();
  const { openModal, closeModal } = useModalStore();
  const { userInfo } = useAuth();

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const [reportData, setReportData] = useState<any>(null);

  const [loading, setLoading] = useState(false);
  console.log(loading);

  const handleGenerateReport = async () => {
    setLoading(true);
    openModal(<LoadingModal context="AI가 리포트를 생성하고 있어요." />);
    try {
      await new Promise((resolve) => setTimeout(resolve, 5000));
      setReportData(mockData);
      toast({
        title: "리포트가 생성되었습니다!",
        description: "최신 결과를 확인하세요.",
      });
      closeModal();
    } catch {
      toast({ title: "오류 발생", description: "리포트 생성에 실패했습니다." });
    } finally {
      setLoading(false);
    }
  };

  if (!reportData) {
    return (
      <NoReport
        handleGenerateReport={handleGenerateReport}
        currentCount={6} // 실제 데이터로 교체 필요
        requiredCount={5} // 실제 데이터로 교체 필요
      />
    );
  }
  return (
    <div className="mx-auto mb-20 mt-5 w-full p-4 md:max-w-5xl">
      <ReportHeader />
      <div className="flex flex-col gap-10 py-4">
        <ScoreAnalysisSection
          hexagon={reportData.hexagon}
        />

        <CloudSection
          positive={reportData.wordCloud.positive}
          negative={reportData.wordCloud.negative}
        />

        <AISummarySection
          positive={reportData.AISummary.positive}
          negative={reportData.AISummary.negative}
        />
        <div className="flex w-full flex-col gap-4 md:flex-row">
          <div className="flex h-[300px] w-full items-center justify-center rounded-md bg-gray-50 p-4">
            <CompareSection />
          </div>
          <div className="flex h-[300px] w-full items-center justify-center rounded-md bg-gray-50 p-4">
            <HistoryRateSection />
          </div>
        </div>
        {/* <CommunityCard post={mockData[0]} /> */}
      </div>
    </div>
  );
}

const mockData = {
  hexagon: {
    minScore: 1,
    maxScore: 5,
    hexagonData: {
      sympathy: {
        score: 5,
        name: "공감(S)",
        description: "상대의 감정과 입장을 이해하고 존중하는 능력입니다.",
        feedback:
          "타인의 감정을 잘 이해하고 공감하는 능력이 뛰어납니다. 상대방의 입장에서 생각하며 소통하여 팀원들에게 신뢰를 주고, 원활한 협업을 이끌어내는 강점이 있습니다.",
        isPositive: true,
      },
      listening: {
        score: 4.3,
        name: "경청(A)",
        description:
          "상대의 말을 주의 깊게 듣고 의미를 정확히 이해하는 능력입니다.",
        feedback:
          "상대방의 의견을 주의 깊게 듣고 존중하는 태도를 보입니다. 적극적인 경청을 통해 원활한 소통을 가능하게 하며, 팀원들의 신뢰를 얻고 있습니다.",
        isPositive: true,
      },
      expression: {
        score: 3.3,
        name: "표현(E)",
        description: "명확하고 효과적으로 자신의 의견을 전달하는 능력입니다.",
        feedback:
          "자신의 의견을 명확하고 설득력 있게 전달하는 능력이 뛰어납니다. 논리적인 커뮤니케이션을 통해 팀원들과 효과적으로 협력하며, 아이디어를 잘 공유하는 강점이 있습니다.",
        isPositive: true,
      },
      problemSolving: {
        score: 2.7,
        name: "문제해결(PS)",
        description: "최적의 해결책을 찾고 실행하는 능력입니다.",
        feedback:
          "문제를 빠르게 파악하고 체계적으로 해결하는 능력이 뛰어납니다. 창의적인 해결책을 도출하며, 논리적인 사고를 바탕으로 문제를 해결하는 강점이 돋보입니다.",
        isPositive: true,
      },
      conflictResolution: {
        score: 2.0,
        name: "갈등해결(CS)",
        description: "갈등을 조정하고 합의점을 찾아내는 능력입니다.",
        feedback:
          "팀 내 갈등 상황에서 중립적인 태도를 유지하며 원만하게 해결하는 능력이 부족합니다. 감정적인 대응을 줄이고 논리적인 해결책을 고려할 필요가 있습니다.",
        isPositive: false,
      },
      leadership: {
        score: 2.3,
        name: "리더십(L)",
        description: "팀을 이끌고 조율하여 목표를 달성하는 능력입니다.",
        feedback:
          "팀원들을 효과적으로 이끌고 조율하는 리더십이 부족한 편입니다. 보다 명확한 방향을 제시하고 책임감을 갖춘 리더십을 발휘할 필요가 있습니다.",
        isPositive: false,
      },
    },
  },

  progress: {
    minScore: 0,
    maxScore: 100,
    progressData: {
      sympathy: { name: "공감(S)", score: 11 },
      listening: { name: "경청(A)", score: 21 },
      expression: { name: "표현(E)", score: 31 },
      problemSolving: { name: "문제해결(PS)", score: 51 },
      conflictResolution: { name: "갈등해결(CS)", score: 81 },
      leadership: { name: "리더십(L)", score: 91 },
    },
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
      // ... existing code ...
      '팀원들의 주관식 응답을 분석한 결과, 개선이 필요한 몇 가지 의견들이 수집되었습니다. 첫째, 의사소통 방식에 있어 회의 시간이 다소 길어지는 경향이 있으며, "의견 제시 후 피드백을 받기까지 시간이 오래 걸린다"는 의견이 있었습니다. 둘째, 업무 프로세스 측면에서는 업무 우선순위 설정이 때때로 불명확하여 일정 관리에 어려움을 겪는다는 의견이 있었습니다. 특히 급한 업무가 갑자기 추가될 때 기존 일정 조정이 원활하지 않다는 점이 지적되었습니다. 마지막으로, 기술 스택 학습을 위한 시간이 충분히 확보되지 않는다는 의견이 있었으며, 팀 내 기술 공유 세션의 횟수를 늘리면 좋겠다는 건설적인 제안이 있었습니다. 이러한 피드백들은 대체로 현재 잘 운영되고 있는 시스템을 더욱 개선하기 위한 제안사항들로 보입니다.',
    // ... existing code ...
  },
};

const LoadingModal = ({ context }: { context: string }) => {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      {/* 어두운 오버레이 배경 */}
      <div className="absolute inset-0 bg-black/50" />

      {/* 로딩 컨텐츠 */}
      <div className="relative z-10 flex flex-col items-center justify-center rounded-lg">
        <Image
          src={"/images/logo-lg.png"}
          alt="loading"
          width={100}
          height={100}
          className="animate-custom-pulse"
        />
        <span className="mt-4 flex text-2xl font-bold text-gray-300">
          <p className="relative">{context}</p>
        </span>
      </div>
    </div>
  );
};
