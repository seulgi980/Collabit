"use client";
import ProjectInput from "@/entities/common/ui/SearchBar";
import { useGithubOrgs } from "@/features/project/api/useGithubOrgs";
import ProjectCreateCard from "@/features/project/ui/ProjectCreateCard";
import {
  GithubOrgResponse,
  GithubRepoResponse,
} from "@/shared/types/response/project";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/shared/ui/select";
import { useState } from "react";

export default function Page() {
  const githubId = "seon318";

  const { githubUserOrgs, isLoading } = useGithubOrgs(githubId, "id");
  console.log(githubId);
  const [organization, setOrganization] = useState("seon318");

  const [title, setTitle] = useState("");
  const [keyword, setKeyword] = useState("");

  const handleOrganization = (value: string) => {
    setOrganization(value);
  };

  return (
    <div className="mx-auto flex w-full flex-col justify-center gap-5 py-10 md:w-[540px]">
      <div className="flex items-center justify-between gap-2">
        <Select defaultValue={organization} onValueChange={handleOrganization}>
          <SelectTrigger className="w-[120px]">
            <SelectValue placeholder="조직" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value={githubId}>{githubId}</SelectItem>
            {isLoading ? (
              <SelectItem value="loading" disabled>
                로딩 중...
              </SelectItem>
            ) : (
              githubUserOrgs?.map((org: GithubOrgResponse) => (
                <SelectItem key={org.login} value={org.login}>
                  {org.login}
                </SelectItem>
              ))
            )}
          </SelectContent>
        </Select>
        <ProjectInput keyword={keyword} setKeyword={setKeyword} />
      </div>

      {/* {projectCreate.map((project) => {
        return <ProjectCreateCard key={project.code} project={project} />;
      })} */}
    </div>
  );
}
