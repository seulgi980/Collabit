"use client";

import { getAddedProjectAPI, createProjectAPI } from "@/shared/api/project";
import { ProjectCreateRequest } from "@/shared/types/request/project";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { useRouter } from "next/navigation";
import { useState, useEffect } from "react";
import { ProjectAddedResponse } from "@/shared/types/response/project";

export const useCreateProject = (org: string, title: string) => {
  const router = useRouter();
  const queryClient = useQueryClient();
  const [isAdded, setIsAdded] = useState(false);

  const { data: addedProjects, isLoading: isAddedLoading } = useQuery<
    ProjectAddedResponse[]
  >({
    queryKey: ["addedProjects"],
    queryFn: getAddedProjectAPI,
  });

  useEffect(() => {
    if (addedProjects) {
      const projectArray = Array.isArray(addedProjects) ? addedProjects : [];
      setIsAdded(
        projectArray.some(
          (project: ProjectAddedResponse) =>
            project.organization === org && project.title === title,
        ),
      );
    }
  }, [addedProjects, org, title]);

  const createProjectMutation = useMutation({
    mutationFn: createProjectAPI,
    onSuccess: () => {
      queryClient.invalidateQueries({
        queryKey: ["addedProjects"],
      });
      queryClient.invalidateQueries({
        queryKey: ["projectList"],
      });
      router.push("/project");
    },
    onError: (error) => console.error("프로젝트 생성 실패:", error),
  });

  const handleCreateProject = async (project: ProjectCreateRequest) => {
    if (isAdded) {
      alert("이미 등록된 프로젝트입니다.");
      return;
    }

    if (project.contributors.length === 1) {
      alert("개인 프로젝트는 등록할 수 없습니다.");
      return;
    }

    await createProjectMutation.mutateAsync(project);
  };

  return {
    isLoading: isAddedLoading,
    isAdded,
    handleCreateProject,
  };
};
