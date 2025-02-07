"use client";
import ChatHeader from "@/entities/chat/ui/ChatHeader";
import ChatInput from "@/entities/chat/ui/ChatInput";
import SurveyBubble from "@/entities/survey/ui/SurveyBubble";
import SurveyMultipleSelectButton from "@/entities/survey/ui/SurveyMultipleSelectButton";
import { useAuth } from "@/features/auth/api/useAuth";
import { getSurveyMultipleQueryAPI } from "@/shared/api/survey";
import { useSurveyStore } from "@/shared/lib/stores/surveyStore";
import { Button } from "@/shared/ui/button";
import generateGreetingMessage from "@/shared/utils/generateGreetingMessage";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";

const SurveyRoom = ({ id }: { id: number }) => {
  const { userInfo } = useAuth();
  const [message, setMessage] = useState("");

  /* 초기 디테일 데이터 불러오기 */
  const setId = useSurveyStore((state) => state.setId);
  const surveyDetail = useSurveyStore((state) => state.surveyDetail);

  const scores = useSurveyStore((state) => state.scores);
  console.log(scores);

  // 디테일 스토어 업데이트
  useEffect(() => {
    setId(id);
  }, [id, setId]);

  /* 설문 스텝 관리 */
  const [currentStep, setCurrentStep] = useState(-1);

  const handleStartSurvey = () => {
    setCurrentStep(0);
  };
  const handleEndSurvey = () => {
    setCurrentStep((prev) => prev + 1);
  };
  const handleSendMessage = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setMessage("");
    setCurrentStep((prev) => prev + 1);
  };
  // 객관식 설문 리스트
  const { data: surveyMultipleQuery } = useQuery({
    queryKey: ["surveyMultiple"],
    queryFn: () => getSurveyMultipleQueryAPI(),
    enabled: !!userInfo && !!id && currentStep === 0,
    staleTime: 1000 * 60,
  });

  const handleSelectAnswer = (currentIndex: number) => {
    if (currentIndex === currentStep) {
      setCurrentStep((prev) => prev + 1);
    }
  };

  // 유저 정보가 없거나 디테일이 없으면 렌더링 안함
  if (!userInfo || !surveyDetail) {
    return null;
  }
  return (
    <div className="flex h-screen w-full flex-col gap-3 py-4 md:h-[calc(100vh-108px)] md:px-2">
      <ChatHeader
        nickname={surveyDetail?.nickname}
        projectName={surveyDetail?.title}
        profileImage={surveyDetail?.profileImage}
      />
      <div className="flex h-[calc(100vh-256px)] w-full flex-col-reverse overflow-y-auto bg-white px-2 py-3 md:px-4">
        <div className="flex flex-col-reverse gap-6">
          {/*객관식 마지막 메시지 */}
          {currentStep >= 24 && (
            <SurveyBubble
              isMe={false}
              message={`${surveyDetail?.nickname}님의 설문 완료 메시지`}
              animation={currentStep === 24}
              isLoading={false}
              component={
                <Button
                  disabled={currentStep > 24}
                  className="fade-in-duration-1000 animate-in fade-in-0 slide-in-from-bottom-4"
                  onClick={handleEndSurvey}
                >
                  대화 시작하기
                </Button>
              }
            />
          )}
          {/* 우선 스텝에 따른 챗봇의 메세지 렌더링 */}
          {surveyMultipleQuery
            ?.slice(0, currentStep + 1)
            .reverse()
            .map((item, index) => {
              const reversedIndex = currentStep - index;
              return (
                <SurveyBubble
                  key={item.questionNumber}
                  isMe={false}
                  message={item.questionText}
                  isLoading={false}
                  component={
                    <SurveyMultipleSelectButton
                      index={reversedIndex}
                      onClick={() => handleSelectAnswer(reversedIndex)}
                    />
                  }
                  animation={reversedIndex === currentStep}
                />
              );
            })}
          {/* 시작 메시지 */}
          <SurveyBubble
            isMe={false}
            message={generateGreetingMessage(
              userInfo.nickname,
              surveyDetail?.nickname ?? "",
              surveyDetail?.title ?? "",
            )}
            animation={currentStep === -1}
            isLoading={false}
            component={
              <Button
                disabled={currentStep >= 0}
                className="fade-in-duration-1000 animate-in fade-in-0 slide-in-from-bottom-4"
                onClick={handleStartSurvey}
              >
                시작하기
              </Button>
            }
          />
        </div>
      </div>
      <ChatInput
        disabled={currentStep < 25}
        message={message}
        setMessage={setMessage}
        handleSendMessage={handleSendMessage}
      />
    </div>
  );
};

export default SurveyRoom;
