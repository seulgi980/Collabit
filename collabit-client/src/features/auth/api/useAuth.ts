import { logoutAPI } from "@/shared/api/auth";
import { getUserInfoAPI } from "@/shared/api/user";
import { useQuery, useQueryClient } from "@tanstack/react-query";
import { useRouter } from "next/navigation";

export const useAuth = () => {
  const queryClient = useQueryClient();
  const router = useRouter();

  const { data, isLoading, isError } = useQuery({
    queryKey: ["auth"],
    queryFn: getUserInfoAPI,
    staleTime: 1000 * 60 * 5,
    gcTime: 1000 * 60 * 60,
    retry: false,
  });

  if (isError) {
    queryClient.setQueryData(["auth"], {
      userInfo: null,
      isAuthenticated: false,
    });
  }

  const logout = async () => {
    try {
      await logoutAPI();
      queryClient.setQueryData(["auth"], {
        userInfo: undefined,
        isAuthenticated: false,
      });
      router.refresh();
    } catch (error) {
      console.error(error);
    }
  };

  return {
    userInfo: data?.userInfo,
    isAuthenticated: data?.isAuthenticated,
    isLoading,
    isError,
    logout,
  };
};
