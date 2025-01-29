"use client";

import ProjectHeader from "@/entities/project/ui/ProjectHeader";
import ProjectInput from "@/entities/project/ui/ProjectInput";
import { useState } from "react";

export default function Page() {
  const [keyword, setKeyword] = useState("");

  const handleSearchKeyword = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    console.log(keyword);
    setKeyword("");
  };

  return (
    <div className="mx-auto flex w-full flex-col justify-center gap-5 py-10 md:w-[580px]">
      <ProjectHeader
        mainTitle="나의 프로젝트"
        subTitle="동료들에게 피드백 받은 현황을 확인할 수 있어요."
        isList={true}
      />
      <div className="flex items-center justify-between gap-2">
        <ProjectInput
          keyword={keyword}
          setKeyword={setKeyword}
          handleSearchKeyword={handleSearchKeyword}
        />
      </div>
    </div>
  );
}
