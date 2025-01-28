"use client";
import useCredentialLogin from "@/features/auth/api/useCredentialLogin";
import { Button } from "@/shared/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
} from "@/shared/ui/form";
import { Input } from "@/shared/ui/input";
import { Loader2 } from "lucide-react";
import Link from "next/link";

const LoginCredentialPage = () => {
  const { form, handleLogin } = useCredentialLogin();

  return (
    <div className="mx-auto flex w-full max-w-[400px] flex-col items-center justify-center gap-10 py-20">
      <div className="flex flex-col items-center justify-center gap-3">
        <h2 className="text-4xl font-bold">로그인</h2>
        <p className="text-md text-center text-gray-600">
          피드백을 진행하려면
          <br /> Github 계정으로 로그인해주세요.
        </p>
      </div>
      <Form {...form}>
        <form
          className="flex w-full flex-col items-center justify-center gap-2"
          onSubmit={handleLogin}
        >
          <FormField
            control={form.control}
            name="email"
            render={({ field }) => (
              <FormItem className="flex w-full items-center gap-2">
                <FormLabel htmlFor="email" className="w-20 font-semibold">
                  이메일
                </FormLabel>
                <FormControl>
                  <Input
                    className="text-sm"
                    placeholder="이메일을 입력해주세요."
                    {...field}
                  />
                </FormControl>
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="password"
            render={({ field }) => (
              <FormItem className="flex w-full items-center gap-2">
                <FormLabel htmlFor="password" className="w-20 font-semibold">
                  비밀번호
                </FormLabel>
                <FormControl>
                  <Input
                    type="password"
                    className="text-sm"
                    placeholder="영문, 숫자, 특수문자 포함 8자 이상"
                    {...field}
                  />
                </FormControl>
              </FormItem>
            )}
          />

          <Button className="mt-10 h-12 w-full" type="submit">
            {form.formState.isSubmitting ? (
              <Loader2 className="animate-spin" />
            ) : (
              "로그인"
            )}
          </Button>
          <div className="mt-10 flex flex-col items-center justify-center gap-2 text-sm text-gray-500">
            <p>
              동료 피드백을 진행하려면?{" "}
              <Link href="/login">
                <span className="text-violet-700">Github 로그인</span>
              </Link>
            </p>
            <p className="text-gray-500">
              <Link href="/signup">
                <span>회원가입</span>
              </Link>{" "}
              |{" "}
              <Link href="/change-password">
                <span>비밀번호 찾기</span>
              </Link>
            </p>
          </div>
        </form>
      </Form>
    </div>
  );
};

export default LoginCredentialPage;
