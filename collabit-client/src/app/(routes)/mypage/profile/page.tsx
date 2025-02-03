"use client";

import { useAuth } from "@/features/auth/api/useAuth";
import { Avatar, AvatarFallback, AvatarImage } from "@/shared/ui/avatar";
import { Button } from "@/shared/ui/button";
import Image from "next/image";
import Link from "next/link";

const ProfilePage = () => {
  const { userInfo } = useAuth();

  const handleExit = () => {
    console.log("회원탈퇴");
  };
  return (
    <div className="flex w-full max-w-[400px] flex-col">
      <div className="border-b py-4">
        <div className="flex gap-2 pb-4">
          <Avatar>
            <AvatarImage src={userInfo?.profileImage} />
            <AvatarFallback>{userInfo?.nickname.slice(0, 2)}</AvatarFallback>
          </Avatar>
          <div className="flex flex-col">
            <span className="font-semibold">{userInfo?.nickname}</span>
            <div className="flex items-center gap-1">
              <Image
                src="/icons/github-sm.svg"
                alt="github"
                width={16}
                height={16}
              />
              {userInfo?.githubId ? (
                <a
                  className="text-xs text-gray-400 hover:text-gray-600"
                  href={`https://github.com/${userInfo?.githubId}`}
                  target="_blank"
                  rel="noopener noreferrer"
                  aria-label="GitHub 프로필 열기"
                >
                  @{userInfo?.githubId}
                </a>
              ) : (
                <Button
                  onClick={() => {
                    console.log("Github 계정 연동하기");
                  }}
                  className="h-4 p-0 text-xs text-gray-400 hover:text-gray-600"
                  variant="ghost"
                >
                  Github 계정 연동하기
                </Button>
              )}
            </div>
          </div>
        </div>
        <div className="flex w-full flex-col gap-2 md:flex-row md:gap-4">
          <Button
            onClick={() => {
              console.log("프로필 이미지 수정");
            }}
            variant="outline"
            className="w-full"
          >
            프로필 이미지 수정
          </Button>
          <Button
            onClick={() => {
              console.log("닉네임 변경");
            }}
            variant="outline"
            className="w-full"
          >
            닉네임 변경
          </Button>
        </div>
      </div>
      <div className="mt-4 flex flex-col gap-2 md:gap-4">
        <Link
          className="block px-1 py-3 font-semibold hover:bg-accent"
          href="/mypage/change-password"
        >
          비밀번호 변경
        </Link>
        <button
          onClick={handleExit}
          className="px-1 py-3 text-left font-semibold hover:bg-accent"
        >
          회원탈퇴
        </button>
      </div>
    </div>
  );
};

export default ProfilePage;
