import HexagonChart from "@/entities/chart/ui/HexagonChart";

import { ChartResponse } from "@/shared/types/response/report";
import { Badge } from "@/shared/ui/badge";

interface HexagonSectionProps {
  hexagon: ChartResponse;
}

const HexagonSection = ({ hexagon }: HexagonSectionProps) => {
  const {
    listening,
    sympathy,
    expression,
    conflictResolution,
    problemSolving,
    leadership,
  } = hexagon;

  const hexagonItems = [
    { key: "sympathy", ...sympathy, position: "col-start-2 row-start-1" },

    {
      key: "listening",
      ...listening,
      position: "col-start-3 row-start-2",
    },

    { key: "expression", ...expression, position: "col-start-3 row-start-3" },
    {
      key: "problemSolving",
      ...problemSolving,
      position: "col-start-2 row-start-4",
    },
    {
      key: "conflictResolution",
      ...conflictResolution,
      position: "col-start-1 row-start-3",
    },

    {
      key: "leadership",
      ...leadership,
      position: "col-start-1 row-start-2",
    },
  ];

  return (
    <div>
      <div className="grid grid-cols-2 justify-center gap-2 md:grid-cols-6 md:grid-rows-1">
        {hexagonItems.map(({ key, name, description }) => (
          <div
            key={key}
            className={`flex max-w-40 flex-col items-center justify-center rounded-xl bg-violet-50 p-2 text-center text-sm text-gray-600`}
          >
            <Badge className="mb-2 text-nowrap bg-violet-400 text-white">
              {name}
            </Badge>
            {description && (
              <span className="flex-1 text-balance text-xs">{description}</span>
            )}
          </div>
        ))}
      </div>
      <div className="md:grid-rows-auto m-auto flex w-full flex-col items-center justify-center gap-4 md:grid md:max-w-4xl md:grid-cols-[auto_350px_auto] md:grid-rows-3 md:place-items-center">
        {/* 중앙 Chart */}
        <div className="col-start-2 row-span-2 row-start-2 flex h-[350px] w-[350px] items-center justify-center">
          <div className="h-[350px] w-[350px]">
            <HexagonChart hexagon={hexagon} />
          </div>
        </div>

        {/* 역량별 피드백 */}
        {hexagonItems.map(({ key, name, feedback, position, positive }) => (
          <div
            key={key}
            className={`text-center text-sm text-gray-600 ${position} m-auto`}
          >
            <Badge
              className={`md:text-md mb-2 ${positive ? "bg-blue-400" : "bg-red-400"} text-white`}
            >
              {name}
            </Badge>
            {feedback && (
              <span className="text-pretty] block text-xs">{feedback}</span>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default HexagonSection;
