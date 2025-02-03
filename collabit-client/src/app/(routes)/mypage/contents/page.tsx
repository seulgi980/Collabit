"use client";

import { useAuth } from "@/features/auth/api/useAuth";
import { useRouter } from "next/navigation";
import { useEffect } from "react";

const ContentsPage = () => {
  const { isAuthenticated, isLoading } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (!isLoading && !isAuthenticated) {
      router.push("/login");
    }
  }, [isAuthenticated, isLoading, router]);

  return <div>ContentsPage</div>;
};

export default ContentsPage;
