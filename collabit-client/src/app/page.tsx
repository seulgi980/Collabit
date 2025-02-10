"use client";

import CompareScoreSection from "@/features/main/CompareScoreSection";
import HotIssueSection from "@/features/main/HotIssueSection";
import MyProjectSection from "@/features/main/MyProjectSection";
import { getProjectListAPI } from "@/shared/api/project";
import { ProjectListResponse } from "@/shared/types/response/project";
import { useQuery } from "@tanstack/react-query";
import { useState } from "react";

import { useEffect } from "react";

export default function Home() {
  const [isMounted, setIsMounted] = useState(false);

  const keyword = "";
  const sort = "";

  useEffect(() => {
    setIsMounted(true);
  }, []);

  const { data: projects } = useQuery<ProjectListResponse>({
    queryKey: ["projectList", keyword, sort],
    queryFn: () => getProjectListAPI({ keyword, sort }),
  });

  const projectList: ProjectListResponse = Array.isArray(projects)
    ? projects
    : [];

  if (!isMounted) return null;

  return (
    <div className="m-auto flex max-w-5xl flex-col items-center gap-11 py-5 md:py-10">
      <h2 className="sr-only">
        메인페이지, 사용자 평균 협업 점수와 프로젝트 소식과 요즘 핫한 소식을
        확인하세요.
      </h2>
      <CompareScoreSection />

      <MyProjectSection />

      <HotIssueSection />
    </div>
  );
}
