import CompareScoreSection from "@/features/main/CompareScoreSection";
import HotIssueSection from "@/features/main/HotIssueSection";
import MyProjectSection from "@/features/main/MyProjectSection";

export default function Home() {
  return (
    <div className="flex flex-col items-center gap-4 py-5 md:py-10">
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
