"use client";
import ChatHeader from "@/entities/chat/ui/ChatHeader";
import ChatInput from "@/entities/chat/ui/ChatInput";
import SurveyBubble from "@/entities/survey/ui/SurveyBubble";
import SurveyMultipleSelectButton from "@/entities/survey/ui/SurveyMultipleSelectButton";
import { useAuth } from "@/features/auth/api/useAuth";
import { getSurveyDetailAPI } from "@/shared/api/survey";
import { Button } from "@/shared/ui/button";
import generateGreetingMessage from "@/shared/utils/generateGreetingMessage";
import { useQuery } from "@tanstack/react-query";
import { useState } from "react";

const SurveyRoom = ({ id }: { id: number }) => {
  const { userInfo } = useAuth();
  const [message, setMessage] = useState("");
  const handleSendMessage = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    console.log(message);
    setMessage("");
  };
  const { data: surveyDetail } = useQuery({
    queryKey: ["surveyDetail", id],
    queryFn: () => getSurveyDetailAPI(id),
  });
  console.log(surveyDetail);
  const TEMP_CHAT_LIST = [
    {
      id: 1,
      message:
        "오후 12:02 얼마나 길까 오후 12:02 얼마나 길까 오후 12:02 얼마나 길까 오후 12:02 얼마나 길까 오후 12:02 얼마나 길까",
      date: "오후 12:02",
      isMe: false,
      userInfo: {
        name: "Name",
        profileImage: "https://github.com/shadcn.png",
      },
    },
    {
      id: 2,
      message: id.toString(),
      date: "오후 12:01",
      isMe: true,
      userInfo: undefined,
    },
    {
      id: 3,
      message: "이건 나",
      date: "오후 12:01",
      isMe: true,
      userInfo: undefined,
    },
    {
      id: 4,
      message: "이건 너",
      date: "오후 12:00",
      isMe: false,
      userInfo: {
        name: "Name",
        profileImage: "https://github.com/shadcn.png",
      },
    },
    {
      id: 5,
      message: "이건 너",
      date: "오후 12:00",
      isMe: false,
      userInfo: {
        name: "Name",
        profileImage: "https://github.com/shadcn.png",
      },
    },
    {
      id: 6,
      message: "이건 너",
      date: "오후 12:00",
      isMe: false,
      userInfo: {
        name: "Name",
        profileImage: "https://github.com/shadcn.png",
      },
    },
    {
      id: 7,
      message: "이건 너",
      date: "오후 12:00",
      isMe: false,
      userInfo: {
        name: "Name",
        profileImage: "https://github.com/shadcn.png",
      },
    },
    {
      id: 8,
      message: "이건 너",
      date: "오후 12:00",
      isMe: false,
      userInfo: {
        name: "Name",
        profileImage: "https://github.com/shadcn.png",
      },
    },
  ];
  if (!userInfo) {
    return null;
  }
  return (
    <div className="flex h-screen w-full flex-col gap-3 py-4 md:h-[calc(100vh-108px)] md:px-2">
      <ChatHeader />
      <div className="flex w-full flex-1 flex-col-reverse gap-4 overflow-y-auto rounded-lg bg-white px-2 py-3 md:px-4">
        <SurveyBubble
          isMe={false}
          message={generateGreetingMessage(
            userInfo!.nickname,
            "이가현",
            "콜라빗",
          )}
          isLoading={false}
          component={<Button onClick={() => {}}>시작하기</Button>}
        />
        {TEMP_CHAT_LIST.map((chat) => (
          <SurveyBubble
            key={chat.id}
            isMe={chat.isMe}
            message={chat.message}
            isLoading={false}
            component={<SurveyMultipleSelectButton />}
            animation={false}
          />
        ))}
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
