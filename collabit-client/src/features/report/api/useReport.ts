import {
  getPortfolioAISummaryAPI,
  getPortfolioChartAPI,
  getPortfolioTimelineChartAPI,
  getPortfolioWordCloudAPI,
} from "@/shared/api/report";
import {
  AISummaryResponse,
  ChartResponse,
  TimelineResponse,
  WordCloudResponse,
} from "@/shared/types/response/report";
import { useQuery } from "@tanstack/react-query";

const useReport = () => {
  const { data: report } = useQuery<ChartResponse, Error>({
    queryKey: ["report"],
    queryFn: () => getPortfolioChartAPI(),
  });

  const { data: wordCloud } = useQuery<WordCloudResponse, Error>({
    queryKey: ["wordCloud"],
    queryFn: () => getPortfolioWordCloudAPI(),
  });

  const { data: summary } = useQuery<AISummaryResponse, Error>({
    queryKey: ["summary"],
    queryFn: () => getPortfolioAISummaryAPI(),
  });

  const { data: timeline } = useQuery<TimelineResponse, Error>({
    queryKey: ["timeline"],
    queryFn: () => getPortfolioTimelineChartAPI(),
  });

  return { report, wordCloud, summary, timeline };
};

export default useReport;
