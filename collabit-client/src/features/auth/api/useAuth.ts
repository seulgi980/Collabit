import { logoutAPI } from "@/shared/api/auth";
import { getUserInfoAPI } from "@/shared/api/user";
import { useQuery, useQueryClient } from "@tanstack/react-query";

export const useAuth = () => {
  const queryClient = useQueryClient();
  const { data, isLoading, isError } = useQuery({
    queryKey: ["auth"],
    queryFn: getUserInfoAPI,
    staleTime: 1000 * 60 * 5,
    gcTime: 1000 * 60 * 30,
    retry: 3,
  });

  const logout = async () => {
    try {
      await logoutAPI();
      queryClient.setQueryData(["auth"], {
        userInfo: undefined,
        isAuthencicated: false,
      });
    } catch (error) {
      console.error(error);
    }
  };

  return {
    userInfo: data?.userInfo,
    isAuthencicated: data?.isAuthencicated,
    isLoading,
    isError,
    logout,
  };
};
