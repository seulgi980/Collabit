"use client";
import { Button } from "@/shared/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/shared/ui/form";
import { Input } from "@/shared/ui/input";
import { InputOTP, InputOTPGroup, InputOTPSlot } from "@/shared/ui/input-otp";
import { Progress } from "@/shared/ui/progress";
import { zodResolver } from "@hookform/resolvers/zod";
import { REGEXP_ONLY_DIGITS } from "input-otp";
import { useForm } from "react-hook-form";
import { z } from "zod";
const formSchema = z.object({
  email: z.string().email(),
  code: z.string().min(6, { message: "인증번호는 6자리입니다." }),
  password: z
    .string()
    .min(8, { message: "비밀번호는 영문, 숫자를 포함한 8자리 이상입니다." }),
  passwordConfirm: z.string().min(8, {
    message: "비밀번호는 영문, 숫자를 포함한 8자리 이상입니다.",
  }),
  nickname: z.string().min(2, { message: "닉네임은 2자리 이상입니다." }),
});

export default function Page() {
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      email: "",
      code: "",
      password: "",
      passwordConfirm: "",
      nickname: "",
    },
  });

  const onSubmit = (data: z.infer<typeof formSchema>) => {
    console.log(data);
  };

  return (
    <div className="flex w-full flex-col items-center justify-center gap-10 py-20">
      <h2 className="text-4xl font-bold">회원가입</h2>
      <Progress className="bg-gray-200" value={25} />
      {/* 폼시작 */}
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)}>
          <div className="flex flex-col items-center gap-2">
            <FormField
              control={form.control}
              name="email"
              render={({ field }) => (
                <FormItem className="flex items-center gap-2">
                  <FormLabel className="w-24">이메일</FormLabel>
                  <FormControl>
                    <Input
                      className="text-sm"
                      placeholder="이메일을 입력해주세요."
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <Button
              onClick={(e) => {
                e.preventDefault();
                console.log("클릭");
              }}
            >
              인증번호 발송
            </Button>
          </div>

          <div className="flex flex-col items-center gap-2">
            <FormField
              control={form.control}
              name="code"
              render={({ field }) => (
                <FormItem className="flex items-center gap-2">
                  <FormLabel className="w-24">인증</FormLabel>
                  <FormControl>
                    <InputOTP
                      maxLength={6}
                      pattern={REGEXP_ONLY_DIGITS}
                      {...field}
                    >
                      <InputOTPGroup>
                        <InputOTPSlot index={0} />
                        <InputOTPSlot index={1} />
                        <InputOTPSlot index={2} />
                        <InputOTPSlot index={3} />
                        <InputOTPSlot index={4} />
                        <InputOTPSlot index={5} />
                      </InputOTPGroup>
                    </InputOTP>
                  </FormControl>
                </FormItem>
              )}
            />

            <Button>다음</Button>
          </div>

          <div className="flex flex-col items-center gap-2">
            <FormField
              control={form.control}
              name="password"
              render={({ field }) => (
                <FormItem className="flex items-center gap-2">
                  <FormLabel className="w-24">비밀번호</FormLabel>
                  <FormControl>
                    <Input
                      className="text-sm"
                      placeholder="비밀번호를 입력해주세요."
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="passwordConfirm"
              render={({ field }) => (
                <FormItem className="flex items-center gap-2">
                  <FormLabel className="w-24">비밀번호 확인</FormLabel>
                  <FormControl>
                    <Input
                      className="text-sm"
                      placeholder="비밀번호를 다시 입력해주세요."
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <Button>다음</Button>
          </div>
          <div className="flex flex-col items-center gap-2">
            <FormField
              control={form.control}
              name="nickname"
              render={({ field }) => (
                <FormItem className="flex items-center gap-2">
                  <FormLabel className="w-24">닉네임</FormLabel>
                  <FormControl>
                    <Input
                      className="text-sm"
                      placeholder="닉네임을 입력해주세요."
                      {...field}
                    />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <Button type="submit">가입하기</Button>
          </div>
        </form>
      </Form>
    </div>
  );
}
