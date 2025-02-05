import { Line } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
);

const HistoryRateSection = () => {
  // 임시 데이터 (실제로는 API나 props로 받아와야 함)
  const projectHistory = [
    {
      name: "관통1 (24.09)",
      skills: { S: 30, A: 60, E: 30, PS: 90, CS: 20, L: 30 },
    },
    {
      name: "관통2 (24.10)",
      skills: { S: 60, A: 70, E: 20, PS: 90, CS: 50, L: 20 },
    },
    {
      name: "파이널 (24.11)",
      skills: { S: 80, A: 75, E: 10, PS: 90, CS: 10, L: 50 },
    },
  ];

  const options = {
    responsive: true,
    plugins: {
      legend: {
        position: "top" as const,
      },
    },
    scales: {
      y: {
        min: 0,
        max: 100,
      },
    },
  };

  const labels = projectHistory.map((project) => project.name);

  const skillColors = {
    S: "rgb(255, 99, 132)",
    A: "rgb(54, 162, 235)",
    E: "rgb(75, 192, 192)",
    PS: "rgb(255, 206, 86)",
    CS: "rgb(153, 102, 255)",
    L: "rgb(255, 159, 64)",
  };

  const skillNames = {
    S: "공감(S)",
    A: "경청(A)",
    E: "표현(E)",
    PS: "문제해결(PS)",
    CS: "갈등해결(CS)",
    L: "리더십(L)",
  };

  const data = {
    labels,
    datasets: Object.entries(skillNames).map(([key, name]) => ({
      label: name,
      data: projectHistory.map(
        (project) => project.skills[key as keyof typeof skillNames],
      ),
      borderColor: skillColors[key as keyof typeof skillColors],
      backgroundColor: skillColors[key as keyof typeof skillColors],
      tension: 0.3,
    })),
  };

  return (
    <div className="h-full w-full">
      <h2 className="mb-4 text-xl font-bold">역량 변화 추이</h2>
      <Line options={options} data={data} />
    </div>
  );
};

export default HistoryRateSection;
