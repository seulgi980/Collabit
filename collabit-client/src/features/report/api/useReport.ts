import {
  getPortfolioAISummaryAPI,
  getPortfolioChartAPI,
  getPortfolioStatusAPI,
  getPortfolioTimelineChartAPI,
  getPortfolioWordCloudAPI,
} from "@/shared/api/report";
import {
  AISummaryResponse,
  ChartResponse,
  ReportStatusResponse,
  TimelineResponse,
  WordCloudResponse,
} from "@/shared/types/response/report";
import { useQuery } from "@tanstack/react-query";

const useReport = () => {
  const { data: reportStatus } = useQuery<ReportStatusResponse, Error>({
    queryKey: ["reportStatus"],
    queryFn: () => getPortfolioStatusAPI(),
  });

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

  return { reportStatus, report, wordCloud, summary, timeline };
};

export default useReport;
