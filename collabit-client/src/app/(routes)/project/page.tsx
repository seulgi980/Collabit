"use client";

import ProjectCotnributor from "@/entities/project/ui/ProjectContributor";
import ProjectHeader from "@/entities/project/ui/ProjectHeader";
import ProjectInput from "@/entities/project/ui/ProjectInput";
import ProjectSelect from "@/entities/project/ui/ProjectSelect";
import { useState } from "react";

export default function Page() {
  const orgList = ["organization1", "organization2", "organization3"];

  const isList = false;

  const [selectType, setSelectType] = useState("");
  const [keyword, setKeyword] = useState("");

  const contributors = [
    { githubId: "id1", profileImage: "https://github.com/shadcn.png" },
    { githubId: "id2", profileImage: "https://github.com/shadcn.png" },
    { githubId: "id3", profileImage: "https://github.com/shadcn.png" },
  ];

  const handleSearchKeyword = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    console.log(keyword);
    setKeyword("");
  };

  return (
    <div className="mx-auto flex w-[400px] flex-col justify-center gap-5 py-10 md:w-[580px]">
      <ProjectHeader
        mainTitle="프로젝트 등록"
        subTitle="프로젝트를 등록하고, 동료들에게 피드백을 요청해보세요."
        isList={isList}
      />
      <div className="flex items-center justify-between gap-2">
        {!isList && (
          <ProjectSelect
            nickname="nickname"
            organizations={orgList}
            selectType={selectType}
            setSelectType={setSelectType}
          />
        )}
        <ProjectInput
          keyword={keyword}
          setKeyword={setKeyword}
          handleSearchKeyword={handleSearchKeyword}
        />
      </div>
      <ProjectCotnributor contributors={contributors} />
    </div>
  );
}
