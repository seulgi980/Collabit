"use client";

import {
  getProjectListAPI,
  removeProjectAPI,
  searchProjectAPI,
  updateProjectDoneAPI,
} from "@/shared/api/project";
import useModalStore from "@/shared/lib/stores/modalStore";
import TwoButtonModal from "@/widget/ui/modals/TwoButtonModal";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import { useState } from "react";

export const useProjectList = () => {
  const queryClient = useQueryClient();
  const { openModal, closeModal } = useModalStore();
  const [searchKeyword, setSearchKeyword] = useState("");

  const {
    data: projectList,
    isLoading,
    isError,
  } = useQuery({
    queryKey: ["project"],
    queryFn: getProjectListAPI,
  });

  const { data: searchResults, isFetching: isSearching } = useQuery({
    queryKey: ["searchProject", searchKeyword],
    queryFn: () => searchProjectAPI(searchKeyword),
    enabled: !!searchKeyword,
  });

  const removeProjectMutation = useMutation({
    mutationFn: removeProjectAPI,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["project"] });
      closeModal();
    },
  });

  const finishSurveyMutation = useMutation({
    mutationFn: updateProjectDoneAPI,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["project"] });
      closeModal();
    },
  });

  const handleRemoveProject = (code: number) => {
    openModal(
      <TwoButtonModal
        title="프로젝트를 삭제하시겠습니까?"
        description="이 프로젝트를 삭제하면 복구할 수 없습니다."
        confirmText="삭제"
        cancelText="취소"
        handleConfirm={async () => {
          await removeProjectMutation.mutateAsync({ code });
        }}
      />,
    );
  };

  const handleFinishSurvey = (code: number) => {
    openModal(
      <TwoButtonModal
        title="설문을 종료하시겠습니까?"
        description="이 프로젝트의 설문에 더이상 참여할 수 없습니다."
        confirmText="설문 종료"
        cancelText="취소"
        handleConfirm={async () => {
          await finishSurveyMutation.mutateAsync(code);
        }}
      />,
    );
  };

  return {
    project: searchKeyword ? searchResults : projectList,
    isLoading: isLoading || isSearching,
    isError,
    searchKeyword,
    setSearchKeyword,
    handleFinishSurvey,
    handleRemoveProject,
  };
};
