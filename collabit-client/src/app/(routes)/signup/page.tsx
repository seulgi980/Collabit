"use client";
import SignupSchema from "@/features/types/SignupSchema";
import { useToast } from "@/shared/hooks/use-toast";
import useModalStore from "@/shared/lib/stores/modalStore";
import { Button } from "@/shared/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
} from "@/shared/ui/form";
import { Input } from "@/shared/ui/input";
import { InputOTP, InputOTPGroup, InputOTPSlot } from "@/shared/ui/input-otp";
import { Progress } from "@/shared/ui/progress";
import OneButtonModal from "@/widget/ui/modals/OneButtonModal";
import { zodResolver } from "@hookform/resolvers/zod";
import { REGEXP_ONLY_DIGITS } from "input-otp";
import { ArrowLeftIcon } from "lucide-react";
import { useRouter } from "next/navigation";
import { useState } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";

// 회원가입 폼 스키마

export default function Page() {
  const router = useRouter();
  const { toast } = useToast();

  // 폼 단계 상태
  const [step, setStep] = useState(1);
  const openModal = useModalStore((state) => state.openModal);
  const closeModal = useModalStore((state) => state.closeModal);

  // 회원가입 폼 생성
  const form = useForm<z.infer<typeof SignupSchema>>({
    resolver: zodResolver(SignupSchema),
    defaultValues: {
      email: "",
      code: "",
      password: "",
      passwordConfirm: "",
      nickname: "",
    },
  });

  // 1단계 인증번호 발송 함수
  const handleSendCode = async (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    // 이메일 필드 검증 실행
    const isEmailValid = await form.trigger("email");
    const emailValue = form.getValues("email");
    const emailError = form.getFieldState("email").error;
    if (!isEmailValid || !emailValue || emailError) {
      toast({
        title: emailError?.message,
        variant: "destructive",
      });
      return;
    }

    // Todo : 인증번호 발송 API 호출

    // 인증번호 발송 성공 시
    toast({
      title: "인증번호 발송",
      description: "인증번호는 5분 후 만료됩니다.",
    });
    setStep(2);

    // 인증번호 발송 실패 시
    toast({
      title: "인증번호 발송 실패",
      description: "인증번호 발송에 실패했습니다.",
      variant: "destructive",
    });
  };

  // 2단계 인증번호 검증 함수
  const handleValidCode = async (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    // 인증번호 검증 로직 실행
    const isCodeValid = await form.trigger("code");
    const codeValue = form.getValues("code");
    const codeError = form.getFieldState("code").error;
    if (!isCodeValid || !codeValue || codeError) {
      toast({
        title: codeError?.message,
        variant: "destructive",
      });
      return;
    }
    // Todo : 인증번호 검증 API 호출

    // 인증번호 검증 성공 시
    setStep(3);

    // 인증번호 검증 실패 시
    toast({
      title: "인증번호 검증 실패",
      description: "인증번호가 일치하지 않습니다.",
      variant: "destructive",
    });
  };

  // 3단계 비밀번호 검증 함수
  const handleValidPassword = async (
    e: React.MouseEvent<HTMLButtonElement>,
  ) => {
    e.preventDefault();

    // 비밀번호 검증 로직 실행
    const isPasswordValid = await form.trigger("password");
    const passwordValue = form.getValues("password");
    const passwordError = form.getFieldState("password").error;
    if (!isPasswordValid || !passwordValue || passwordError) {
      toast({
        title: passwordError?.message,
        variant: "destructive",
      });
      return;
    }

    // 비밀번호 확인 검증 로직 실행
    const isPasswordConfirmValid = await form.trigger("passwordConfirm");
    const passwordConfirmValue = form.getValues("passwordConfirm");
    const passwordConfirmError = form.getFieldState("passwordConfirm").error;
    if (
      !isPasswordConfirmValid ||
      !passwordConfirmValue ||
      passwordConfirmError
    ) {
      toast({
        title: passwordConfirmError?.message,
        variant: "destructive",
      });
      return;
    }

    setStep(4);
  };

  // 4단계 닉네임 검증 함수
  const handleValidNickname = async (
    e: React.MouseEvent<HTMLButtonElement>,
  ) => {
    e.preventDefault();
    // 닉네임 검증 로직 실행
    const isNicknameValid = await form.trigger("nickname");
    const nicknameValue = form.getValues("nickname");
    const nicknameError = form.getFieldState("nickname").error;
    if (!isNicknameValid || !nicknameValue || nicknameError) {
      toast({
        title: nicknameError?.message,
        variant: "destructive",
      });
      return;
    }

    // 닉네임 중복 API 호출

    // 닉네임 중복 확인 성공 시
    openModal(
      <OneButtonModal
        title="사용 가능한 닉네임"
        description="회원가입을 진행하시겠습니까?"
        buttonText="가입하기"
        handleButtonClick={() => {
          onSubmit(form.getValues());
        }}
      />,
    );

    // 닉네임 중복 확인 실패 시
    toast({
      title: "사용 불가능한 닉네임",
      description: "이미 사용중인 닉네임입니다.",
      variant: "destructive",
    });
  };

  // 회원가입 폼 제출 함수
  const onSubmit = async (data: z.infer<typeof SignupSchema>) => {
    console.log(data);
    // 회원가입 API 호출

    // 회원가입 성공 시
    openModal(
      <OneButtonModal
        title="회원가입 성공"
        description="회원가입이 완료되었습니다."
        buttonText="로그인"
        handleButtonClick={() => {
          router.push("/login/credential");
          closeModal();
        }}
      />,
    );
  };

  return (
    <div className="mx-auto flex h-[460px] w-full max-w-[400px] flex-col justify-center gap-10 py-20 md:h-[500px]">
      <div className="relative flex w-full items-center justify-center">
        <button
          className="absolute left-0"
          type="button"
          onClick={() => {
            if (step === 1) {
              router.push("/login/credential");
            } else {
              setStep(step - 1);
            }
          }}
        >
          <ArrowLeftIcon className="h-6 w-6" />
        </button>
        <h2 className="text-4xl font-bold">회원가입</h2>
      </div>
      <Progress className="bg-gray-200" value={25 * step} />
      {/* 폼시작 */}
      <Form {...form}>
        <form
          className="flex w-full flex-col items-center gap-2"
          onSubmit={form.handleSubmit(onSubmit)}
        >
          {step === 1 && (
            <div className="flex w-full flex-col items-center gap-2">
              <FormField
                control={form.control}
                name="email"
                render={({ field }) => (
                  <FormItem className="flex w-full flex-col items-center">
                    <div className="flex w-full items-center gap-2">
                      <FormLabel className="w-32 font-semibold">
                        이메일
                      </FormLabel>
                      <FormControl>
                        <Input
                          className="text-sm"
                          placeholder="이메일을 입력해주세요."
                          {...field}
                        />
                      </FormControl>
                    </div>
                  </FormItem>
                )}
              />
              <Button className="mt-8" type="button" onClick={handleSendCode}>
                인증번호 발송
              </Button>
            </div>
          )}

          {step === 2 && (
            <div className="flex w-full flex-col items-center gap-2">
              <FormField
                control={form.control}
                name="code"
                render={({ field }) => (
                  <FormItem className="flex w-full flex-col items-center">
                    <div className="flex w-full items-center justify-center gap-2">
                      <FormLabel className="w-12 font-semibold">인증</FormLabel>
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
                    </div>
                    {/* <FormMessage /> */}
                  </FormItem>
                )}
              />

              <Button className="mt-8" type="button" onClick={handleValidCode}>
                다음
              </Button>
            </div>
          )}

          {step === 3 && (
            <div className="flex w-full flex-col items-center gap-2">
              <FormField
                control={form.control}
                name="password"
                render={({ field }) => (
                  <FormItem className="flex w-full flex-col items-center">
                    <div className="flex w-full items-center gap-2">
                      <FormLabel className="w-36 font-semibold">
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
                    </div>
                    {/* <FormMessage /> */}
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="passwordConfirm"
                render={({ field }) => (
                  <FormItem className="flex w-full flex-col items-center">
                    <div className="flex w-full items-center gap-2">
                      <FormLabel className="w-36 font-semibold">
                        비밀번호 확인
                      </FormLabel>
                      <FormControl>
                        <Input
                          type="password"
                          className="text-sm"
                          placeholder="비밀번호를 다시 입력해주세요."
                          {...field}
                        />
                      </FormControl>
                    </div>
                    {/* <FormMessage /> */}
                  </FormItem>
                )}
              />
              <Button
                className="mt-8"
                type="button"
                onClick={handleValidPassword}
              >
                다음
              </Button>
            </div>
          )}

          {step === 4 && (
            <div className="flex w-full flex-col items-center">
              <FormField
                control={form.control}
                name="nickname"
                render={({ field }) => (
                  <FormItem className="flex w-full flex-col items-center">
                    <div className="flex w-full items-center gap-2">
                      <FormLabel className="w-16 font-semibold">
                        닉네임
                      </FormLabel>
                      <FormControl>
                        <Input
                          className="text-sm"
                          placeholder="닉네임을 입력해주세요."
                          {...field}
                        />
                      </FormControl>
                    </div>
                    {/* <FormMessage /> */}
                  </FormItem>
                )}
              />
              <Button
                className="mt-8"
                type="button"
                onClick={handleValidNickname}
              >
                중복 확인
              </Button>
            </div>
          )}
        </form>
      </Form>
    </div>
  );
}
