"use client";

import { Button } from "@/shared/ui/button";
import { Input } from "@/shared/ui/input";
import { Label } from "@/shared/ui/label";
import Link from "next/link";

const LoginCredentialPage = () => {
  return (
    <div className="mx-auto flex w-full max-w-[400px] flex-col items-center justify-center gap-10 py-20">
      <div className="flex flex-col items-center justify-center gap-3">
        <h2 className="text-4xl font-bold">로그인</h2>
        <p className="text-md text-center text-gray-600">
          피드백을 진행하려면
          <br /> Github 계정으로 로그인해주세요.
        </p>
      </div>
      <div className="flex w-full flex-col items-center justify-center gap-2">
        <div className="flex w-full items-center gap-2">
          <Label htmlFor="email" className="w-20">
            이메일
          </Label>
          <Input
            className="text-sm"
            placeholder="이메일을 입력해주세요."
            id="email"
            type="email"
          />
        </div>

        <div className="flex w-full items-center gap-2">
          <Label htmlFor="password" className="w-20">
            비밀번호
          </Label>
          <Input
            className="text-sm"
            placeholder="비밀번호를 입력해주세요."
            id="password"
            type="password"
          />
        </div>

        <Button className="mt-10 h-12 w-full bg-violet-700 text-white hover:bg-violet-800">
          로그인
        </Button>
        <div className="mt-10 flex flex-col items-center justify-center gap-2 text-sm text-gray-500">
          <p>
            아직 계정이 없으신가요?{" "}
            <Link href="/signup">
              <span className="text-violet-700">회원가입</span>
            </Link>
          </p>
          <p>
            동료 피드백을 진행하려면?{" "}
            <Link href="/login">
              <span className="text-violet-700">Github 로그인</span>
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default LoginCredentialPage;
