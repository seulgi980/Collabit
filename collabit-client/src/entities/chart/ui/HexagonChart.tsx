import React, { useEffect, useRef } from "react";
import { Chart, registerables } from "chart.js";
import { SkillData } from "@/shared/types/response/report";

Chart.register(...registerables);

interface HexagonChartProps {
  hexagon: {
    hexagonData: SkillData;
    minScore: number;
    maxScore: number;
  };
}

const HexagonChart = ({ hexagon }: HexagonChartProps) => {
  const canvasRef = useRef<HTMLCanvasElement>(null);

  useEffect(() => {
    if (!canvasRef.current) return;
    const ctx = canvasRef.current.getContext("2d") as CanvasRenderingContext2D;
    const hexagonChart = new Chart(ctx, {
      type: "radar", // 육각형 차트는 레이더 차트로 구현
      data: {
        labels: Object.values(hexagon.hexagonData).map((skill) => skill.name),
        datasets: [
          {
            label: "역량 점수",
            data: Object.values(hexagon.hexagonData).map(
              (skill) => skill.score,
            ),
            backgroundColor: "rgba(109, 40, 217, 0.2)", // 연한 바이올렛
            borderColor: "#6d28d9",
            borderWidth: 1,
          },
        ],
      },
      options: {
        scales: {
          r: {
            beginAtZero: false,
            max: hexagon.maxScore,
            min: hexagon.minScore,
          },
        },
      },
    });

    return () => {
      hexagonChart.destroy(); // 컴포넌트 언마운트 시 차트 제거
    };
  }, [hexagon]);

  return (
    <div>
      <canvas ref={canvasRef} />
    </div>
  );
};

export default HexagonChart;
