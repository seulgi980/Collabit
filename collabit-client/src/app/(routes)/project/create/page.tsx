"use client";

import ProjectCotnributor from "@/entities/project/ui/ProjectContributor";
import ProjectHeader from "@/entities/project/ui/ProjectHeader";
import ProjectInput from "@/entities/project/ui/ProjectInput";
import ProjectSelect from "@/entities/project/ui/ProjectSelect";
import ProjectAddCard from "@/features/project/ProjectCreateCard";
import { useState } from "react";

export default function Page() {
  const orgList = ["organization1", "organization2", "organization3"];

  const isList = false;

  const [selectType, setSelectType] = useState("");
  const [keyword, setKeyword] = useState("");

  const projects = [
    {
      title: "프로젝트 A",
      contributors: [
        { githubId: "id1", profileImage: "https://github.com/shadcn.png" },
        { githubId: "id2", profileImage: "https://github.com/shadcn.png" },
        { githubId: "id3", profileImage: "https://github.com/shadcn.png" },
      ],
      timestamp: new Date("2024-01-28T10:00:00Z"), // Date 객체로 설정
    },
    {
      title: "프로젝트 B",
      contributors: [
        { githubId: "id4", profileImage: "https://github.com/shadcn.png" },
        { githubId: "id5", profileImage: "https://github.com/shadcn.png" },
      ],
      timestamp: new Date("2024-12-25T14:30:00Z"),
    },
    {
      title: "프로젝트 C",
      contributors: [
        { githubId: "id6", profileImage: "https://github.com/shadcn.png" },
        { githubId: "id7", profileImage: "https://github.com/shadcn.png" },
        { githubId: "id8", profileImage: "https://github.com/shadcn.png" },
        { githubId: "id9", profileImage: "https://github.com/shadcn.png" },
      ],
      timestamp: new Date("2025-01-20T09:15:00Z"),
    },
    {
      title: "프로젝트 D",
      contributors: [
        { githubId: "id10", profileImage: "https://github.com/shadcn.png" },
      ],
      timestamp: new Date("2025-01-22T16:45:00Z"),
    },
    {
      title: "프로젝트 E",
      contributors: [
        { githubId: "id11", profileImage: "https://github.com/shadcn.png" },
        { githubId: "id12", profileImage: "https://github.com/shadcn.png" },
        { githubId: "id13", profileImage: "https://github.com/shadcn.png" },
      ],
      timestamp: new Date("2025-01-18T08:00:00Z"),
    },
  ];

  const handleSearchKeyword = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    console.log(keyword);
    setKeyword("");
  };

  return (
    <div className="mx-auto flex w-full flex-col justify-center gap-5 py-10 md:w-[580px]">
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
      {projects.map((project, index) => {
        return (
          <ProjectAddCard
            key={index}
            contributors={project.contributors}
            title={project.title}
            timestamp={project.timestamp}
          />
        );
      })}
    </div>
  );
}
