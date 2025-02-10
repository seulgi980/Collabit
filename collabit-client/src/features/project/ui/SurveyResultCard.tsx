import HexagonSection from "@/features/report/ui/HexagonSection";
import { getHexagonGraphAPI } from "@/shared/api/project";
import { useQuery } from "@tanstack/react-query";

const SurveyResultCard = ({ code }: { code: number }) => {
  const { data, isLoading } = useQuery({
    queryKey: ["project", code],
    queryFn: () => getHexagonGraphAPI(code),
  });

  if (isLoading) return null;

  return (
    <div>
      <HexagonSection hexagon={data} />
    </div>
  );
};

export default SurveyResultCard;
