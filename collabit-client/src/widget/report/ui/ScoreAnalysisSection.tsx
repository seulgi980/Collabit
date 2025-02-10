import ReportTitle from "@/entities/report/ui/ReportTitle";
import { useAuth } from "@/features/auth/api/useAuth";
import HexagonSection from "@/features/report/ui/HexagonSection";
import { ChartResponse } from "@/shared/types/response/report";

const ScoreAnalysisSection = ({ hexagon }: { hexagon: ChartResponse }) => {
  const { userInfo } = useAuth();

  return (
    <div className="flex flex-col gap-2">
      <ReportTitle
        title="전체 역량 분석"
        description={`동료들이 평가한 점수를 기반으로 ${userInfo?.nickname}님의 협업 역량을 분석하였습니다.`}
      />
      <HexagonSection hexagon={hexagon} />
    </div>
  );
};

export default ScoreAnalysisSection;
