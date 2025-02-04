"use client";

import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import { Bar } from "react-chartjs-2";

ChartJS.register(
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
);

const CompareSection = () => {
  const mySkills = {
    S: 85,
    A: 75,
    E: 90,
    PS: 80,
    CS: 85,
    L: 90,
  };

  const averageSkills = {
    S: 78,
    A: 82,
    E: 75,
    PS: 77,
    CS: 80,
    L: 60,
  };
  const minScore =
    Math.min(...Object.values(mySkills), ...Object.values(averageSkills)) - 10;

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: "top" as const,
      },
      title: {
        display: true,
        text: "협업 능력 비교",
        font: {
          size: 16,
          weight: "bold" as const,
        },
      },
    },
    scales: {
      y: {
        min: minScore <= 0 ? 0 : minScore,
        max: 100,
        ticks: {
          stepSize: 20,
        },
      },
    },
  };

  const labels = [
    "공감(S)",
    "경청(A)",
    "표현(E)",
    "문제해결(PS)",
    "갈등해결(CS)",
    "리더십(L)",
  ];

  const data = {
    labels,
    datasets: [
      {
        label: "나의 점수",
        data: [
          mySkills.S,
          mySkills.A,
          mySkills.E,
          mySkills.PS,
          mySkills.CS,
          mySkills.L,
        ],
        backgroundColor: "rgba(124, 58, 237, 0.8)",
      },
      {
        label: "평균 점수",
        data: [
          averageSkills.S,
          averageSkills.A,
          averageSkills.E,
          averageSkills.PS,
          averageSkills.CS,
          averageSkills.L,
        ],
        backgroundColor: "rgba(209, 213, 219, 0.8)",
      },
    ],
  };

  return (
    // <div className="h-full w-full  p-4">
    <Bar options={options} data={data} />
    // </div>
  );
};

export default CompareSection;
