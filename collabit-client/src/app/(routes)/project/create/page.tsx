"use client";

import ProjectHeader from "@/entities/project/ui/ProjectHeader";
import ProjectInput from "@/entities/project/ui/ProjectInput";
import ProjectSelect from "@/entities/project/ui/ProjectSelect";
import ProjectCreateCard from "@/features/project/ui/ProjectCreateCard";
import { ProjectCreate } from "@/shared/types/model/Project";
import { useState } from "react";

export default function Page() {
  const orgList = ["organization1", "organization2", "organization3"];

  const [selectType, setSelectType] = useState("");
  const [keyword, setKeyword] = useState("");

  const projectCreate = [
    {
      code: 1,
      title: "프로젝트 A",
      contributor: [
        {
          code: 1,
          githubId: "id1",
          profileImage: "https://github.com/shadcn.png",
        },
        {
          code: 2,
          githubId: "id2",
          profileImage: "https://github.com/shadcn.png",
        },
        {
          code: 3,
          githubId: "id3",
          profileImage: "https://github.com/shadcn.png",
        },
      ],
      timestamp: new Date("2024-01-28T10:00:00Z"),
    },
    {
      code: 2,
      title: "프로젝트 B",
      contributor: [
        {
          code: 4,
          githubId: "id4",
          profileImage: "https://github.com/shadcn.png",
        },
        {
          code: 5,
          githubId: "id5",
          profileImage: "https://github.com/shadcn.png",
        },
      ],
      timestamp: new Date("2024-12-25T14:30:00Z"),
    },
    {
      code: 3,
      title: "프로젝트 C",
      contributor: [
        {
          code: 6,
          githubId: "id6",
          profileImage: "https://github.com/shadcn.png",
        },
        {
          code: 7,
          githubId: "id7",
          profileImage: "https://github.com/shadcn.png",
        },
        {
          code: 8,
          githubId: "id8",
          profileImage: "https://github.com/shadcn.png",
        },
        {
          code: 9,
          githubId: "id9",
          profileImage: "https://github.com/shadcn.png",
        },
      ],
      timestamp: new Date("2025-01-20T09:15:00Z"),
    },
    {
      code: 4,
      title: "프로젝트 D",
      contributor: [
        {
          code: 10,
          githubId: "id10",
          profileImage: "https://github.com/shadcn.png",
        },
      ],
      timestamp: new Date("2025-01-22T16:45:00Z"),
    },
    {
      code: 5,
      title: "프로젝트 E",
      contributor: [
        {
          code: 11,
          githubId: "id11",
          profileImage: "https://github.com/shadcn.png",
        },
        {
          code: 12,
          githubId: "id12",
          profileImage: "https://github.com/shadcn.png",
        },
        {
          code: 13,
          githubId: "id13",
          profileImage: "https://github.com/shadcn.png",
        },
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
    <div className="mx-auto flex w-full flex-col justify-center gap-5 py-10 md:w-[540px]">
      <ProjectHeader
        mainTitle="프로젝트 등록"
        subTitle="프로젝트를 등록하고, 동료들에게 피드백을 요청해보세요."
        isList={false}
      />
      <div className="flex items-center justify-between gap-2">
        <ProjectSelect
          nickname="nickname"
          organizations={orgList}
          selectType={selectType}
          setSelectType={setSelectType}
        />
        <ProjectInput
          keyword={keyword}
          setKeyword={setKeyword}
          handleSearchKeyword={handleSearchKeyword}
        />
      </div>
      {projectCreate.map((project) => {
        return <ProjectCreateCard key={project.code} project={project} />;
      })}
    </div>
  );
}
