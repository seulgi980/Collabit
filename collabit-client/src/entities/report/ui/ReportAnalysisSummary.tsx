import { cn } from "@/shared/lib/shadcn/utils";

const ReportAnalysisSummary = ({
  capacity,
  type,
  description,
}: {
  capacity: string;
  type: "positive" | "negative";
  description: string;
}) => {
  return (
    <div>
      <h3 className="text-lg font-bold">
        {type === "positive" ? (
          <span>최고 점수 역량 : </span>
        ) : (
          <span>개선 필요 역량 : </span>
        )}
        <span
          className={cn(
            type === "negative" ? "text-red-600" : "text-green-600",
          )}
        >
          {capacity}
        </span>
      </h3>

      <p className="mt-2 text-sm text-gray-700">{description}</p>
    </div>
  );
};
export default ReportAnalysisSummary;
