"use client";
import SearchBar from "@/entities/common/ui/SearchBar";
import { Button } from "@/shared/ui/button";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/shared/ui/select";
import ProjectList from "@/widget/project/ui/ProjectList";
import ProjectListSkeleton from "@/widget/project/ui/ProjectListSkeleton";
import { useRouter } from "next/navigation";
import { Suspense, useState } from "react";

export default function Page() {
  const router = useRouter();
  const [keyword, setKeyword] = useState("");
  const [sort, setSort] = useState("recent");

  const handleSort = (value: string) => {
    setSort(value);
  };
  const handleCreateProject = () => {
    router.push("/project/create");
  };

  return (
    <div className="flex flex-col gap-4">
      <div className="flex items-center justify-between gap-2">
        <SearchBar keyword={keyword} setKeyword={setKeyword} />
        <Select defaultValue={sort} onValueChange={handleSort}>
          <SelectTrigger className="w-[120px]">
            <SelectValue placeholder="정렬" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="recent">최신순</SelectItem>
            <SelectItem value="participantRatio">참여자 비율</SelectItem>
          </SelectContent>
        </Select>
        <Button onClick={handleCreateProject}>프로젝트 생성</Button>
      </div>
      <Suspense fallback={<ProjectListSkeleton />}>
        <ProjectList keyword={keyword} sort={sort} />
      </Suspense>
    </div>
  );
}
