"use client";
import { useAuth } from "@/features/auth/api/useAuth";
import { redirect } from "next/navigation";

export default function Page() {
  const { isAuthencicated } = useAuth();
  if (!isAuthencicated) {
    redirect("/login");
  }
  redirect("/mypage/profile");
}
