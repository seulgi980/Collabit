import HexagonChart from "@/entities/chart/ui/HexagonChart";
import ReportAnalysisSummary from "@/entities/report/ui/ReportAnalysisSummary";
import ReportTitle from "@/entities/report/ui/ReportTitle";
import { UserInfo } from "@/shared/types/model/User";

const ScoreAnalysisSection = ({
  userInfo,
  chartScore,
  highestSkill,
  lowestSkill,
}: {
  userInfo: UserInfo;
  chartScore: any;
  highestSkill: any;
  lowestSkill: any;
}) => {
  return (
    <div className="flex flex-col gap-4">
      <ReportTitle
        title="전체 역량 분석"
        description={`동료들이 평가한 점수를 기반으로 ${userInfo?.nickname}님의 협업 역량을 분석하였습니다.`}
      />

      <div className="flex flex-col gap-4 md:flex-row">
        <div className="max-w-1/3 md:justify-center">
          <HexagonChart data={chartScore} />
        </div>
        <div className="grid flex-1 grid-rows-2">
          <ReportAnalysisSummary
            capacity={highestSkill.name}
            type="positive"
            description={highestSkill.description}
          />
          <ReportAnalysisSummary
            capacity={lowestSkill.name}
            type="negative"
            description={lowestSkill.description}
          />
        </div>
      </div>
    </div>
  );
};

export default ScoreAnalysisSection;
