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
  const [isStart, setIsStart] = useState(false);

  // 스토어 id업데이트 메서드 선언
  const setId = useSurveyStore((state) => state.setId);

  // Layout에서 넣어준 디테일 데이터 불러오기
  const surveyDetail = useSurveyStore((state) => state.surveyDetail);
  // 디테일 스토어 업데이트
  useEffect(() => {
    setId(id);
  }, [id, setId]);

  const handleSendMessage = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setMessage("");
  };

  const handleStartSurvey = () => {
    setIsStart(true);
  };

  // 객관식 설문 리스트
  const { data: surveyMultipleQuery } = useQuery({
    queryKey: ["surveyMultiple"],
    queryFn: () => getSurveyMultipleQueryAPI(),
    enabled: !!userInfo && !!id && isStart,
    staleTime: 1000 * 60,
  });
  console.log(surveyMultipleQuery);

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
      <div className="flex w-full flex-1 flex-col-reverse gap-4 overflow-y-auto rounded-lg bg-white px-2 py-3 md:px-4">
        {!surveyDetail?.surveyMultipleResponse?.length ? (
          <SurveyBubble
            isMe={false}
            message={generateGreetingMessage(
              userInfo.nickname,
              surveyDetail?.nickname ?? "",
              surveyDetail?.title ?? "",
            )}
            animation={true}
            isLoading={false}
            component={
              <Button
                className="duration-500 animate-in fade-in-0 slide-in-from-bottom-4"
                onClick={handleStartSurvey}
              >
                시작하기
              </Button>
            }
          />
        ) : (
          surveyDetail?.surveyMultipleResponse?.map((item) => (
            <SurveyBubble
              key={item.submittedAt}
              isMe={false}
              message={item.messages[0].content}
              isLoading={false}
              component={<SurveyMultipleSelectButton />}
              animation={false}
            />
          ))
        )}
      </div>
      <ChatInput
        message={message}
        setMessage={setMessage}
        handleSendMessage={handleSendMessage}
      />
    </div>
  );
};

export default SurveyRoom;
