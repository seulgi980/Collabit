"use client";

import {
  getGithubCollaboratorsAPI,
  getGithubUserOrgsAPI,
  getGithubUserReposAPI,
  getAddedProjectAPI,
  createProjectAPI,
} from "@/shared/api/project";
import { ProjectCreateRequest } from "@/shared/types/request/project";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { useRouter } from "next/navigation";
import { useState, useEffect } from "react";

export const useGithubProject = (
  org?: string,
  title?: string,
  userId?: string,
) => {
  const router = useRouter();
  const queryClient = useQueryClient();
  const [isAdded, setIsAdded] = useState(false);

  const { data: githubCollaborators, isLoading: isCollaboratorsLoading } =
    useQuery({
      queryKey: ["githubCollaborators", org, title],
      queryFn: () =>
        org && title
          ? getGithubCollaboratorsAPI(org, title)
          : Promise.resolve([]),
      enabled: !!title,
    });



  const { data: githubUserRepos, isLoading: isUserReposLoading } = useQuery({
    queryKey: ["githubUserRepos", userId],
    queryFn: () =>
      userId ? getGithubUserReposAPI(userId) : Promise.resolve([]),
    enabled: !!userId,
  });

  const { data: addedProjects, isLoading: isAddedLoading } = useQuery({
    queryKey: ["addedProjects"],
    queryFn: getAddedProjectAPI,
  });

  useEffect(() => {
    if (addedProjects?.length && org && title) {
      const isAlreadyAdded = addedProjects.some(
        (project: { organization: string; title: string }) =>
          project.organization === org && project.title === title,
      );
      setIsAdded(isAlreadyAdded);
    }
  }, [addedProjects, org, title]);

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
    githubCollaborators,
    githubUserRepos,
    isLoading:
      isCollaboratorsLoading ||
      isUserReposLoading ||
      isAddedLoading,
    isAdded,
    handleCreateProject,
  };
};
