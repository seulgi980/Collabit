"use client";

import {
  getAddedProjectAPI,
  createProjectAPI,
} from "@/shared/api/project";
import { ProjectTitle } from "@/shared/types/model/Project";
import { ProjectCreateRequest } from "@/shared/types/request/project";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { useRouter } from "next/navigation";
import { useState, useEffect } from "react";

export const useCreateProject = (org: string, title: string) => {
  const router = useRouter();
  const queryClient = useQueryClient();
  const [isAdded, setIsAdded] = useState(false);

  const { data: addedProjects, isLoading: isAddedLoading } = useQuery({
    queryKey: ["addedProjects"],
    queryFn: getAddedProjectAPI,
  });

  useEffect(() => {
    if (addedProjects) {
      setIsAdded(
        addedProjects.some(
          (project: ProjectTitle) =>
            project.organization === org && project.title === title
        )
      );
    }
  }, [addedProjects, org, title]);

  const createProjectMutation = useMutation({
    mutationFn: createProjectAPI,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["addedProjects"] });
      router.push("/project");
    },
    onError: (error) => console.error("프로젝트 생성 실패:", error),
  });

  const handleCreateProject = async (project: ProjectCreateRequest) => {
    if (isAdded) {
      alert("이미 등록된 프로젝트입니다.");
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
