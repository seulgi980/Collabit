"use client";

import {
  getGithubOrgReposAPI,
  getGithubCollaboratorsAPI,
  getGithubUserOrgsAPI,
  getGithubUserReposAPI,
  getAddedProject,
  createProjectAPI,
} from "@/shared/api/project";
import { ProjectCreateRequest } from "@/shared/types/request/Project";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { useRouter } from "next/navigation";
import { useState, useEffect } from "react";

export const useGithubProject = () => {
  const router = useRouter();
  const queryClient = useQueryClient();
  const [searchKeyword, setSearchKeyword] = useState("");
  const [isAdded, setIsAdded] = useState(false);

  // 🔹 깃허브 조직의 저장소 목록 가져오기
  const { data: githubOrgRepos, isLoading: isOrgReposLoading } = useQuery({
    queryKey: ["githubOrgRepos"],
    queryFn: () => getGithubOrgReposAPI(searchKeyword),
    enabled: !!searchKeyword,
  });

  const { data: githubCollaborators, isLoading: isCollaboratorsLoading } = useQuery({
    queryKey: ["githubCollaborators"],
    queryFn: () => getGithubCollaboratorsAPI(searchKeyword, "repo-title"),
    enabled: !!searchKeyword,
  });

  const { data: githubUserOrgs, isLoading: isUserOrgsLoading } = useQuery({
    queryKey: ["githubUserOrgs"],
    queryFn: () => getGithubUserOrgsAPI(searchKeyword),
    enabled: !!searchKeyword,
  });

  const { data: githubUserRepos, isLoading: isUserReposLoading } = useQuery({
    queryKey: ["githubUserRepos"],
    queryFn: () => getGithubUserReposAPI(searchKeyword),
    enabled: !!searchKeyword,
  });

  const { data: addedProjects, isLoading: isAddedLoading } = useQuery({
    queryKey: ["addedProjects"],
    queryFn: getAddedProject,
  });

  useEffect(() => {
    if (addedProjects && searchKeyword) {
      const isAlreadyAdded = addedProjects.some(
        (project: { organization: string; title: string }) =>
          project.organization === searchKeyword && project.title === "repo-title"
      );
      setIsAdded(isAlreadyAdded);
    }
  }, [addedProjects, searchKeyword]);

  const createProjectMutation = useMutation({
    mutationFn: (project: ProjectCreateRequest) => createProjectAPI(project),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["addedProjects"] });
      router.push("/project");
    },
    onError: (error) => {
      console.error("프로젝트 생성 실패:", error);
    },
  });

  const handleCreateProject = async (project: ProjectCreateRequest) => {
    if (isAdded) {
      alert("이미 등록된 프로젝트입니다.");
      return;
    }

    try {
      await createProjectMutation.mutateAsync(project);
    } catch (error) {
      console.error(error);
    }
  };

  return {
    githubOrgRepos,
    githubCollaborators,
    githubUserOrgs,
    githubUserRepos,
    isLoading: isOrgReposLoading || isCollaboratorsLoading || isUserOrgsLoading || isUserReposLoading || isAddedLoading,
    isAdded,
    searchKeyword,
    setSearchKeyword,
    handleCreateProject,
  };
};
