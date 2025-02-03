import ReportTitle from "@/entities/report/ui/ReportTitle";

const AISummarySection = ({
  positive,
  negative,
}: {
  positive: string;
  negative: string;
}) => {
  return (
    <div className="flex flex-col gap-4 bg-white">
      <ReportTitle title="AI 강약점 요약" />
      <div className="flex flex-col gap-2">
        <span className="md:text-md rounded-lg bg-green-50 p-4 text-sm">
          {positive}
        </span>
        <span className="md:text-md rounded-lg bg-red-50 p-4 text-sm">
          {negative}
        </span>
      </div>
    </div>
  );
};
export default AISummarySection;
