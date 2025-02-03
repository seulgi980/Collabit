"use client";
import ChatList from "@/features/chat/ui/ChatList";
import { use } from "react";

interface Params {
  projectId: string;
}

export default function ChatListPage({ params }: { params: Promise<Params> }) {
  const { projectId } = use(params);
  console.log(projectId);

  // TODO: 채팅 목록 조회 API 연동
  const CHATLIST = [
    {
      id: 123,
      unRead: 100,
      participant: {
        name: "라이언",
        profileImage: "https://github.com/shadcn.png",
      },
      description: "안녕하세요",
      date: "1월 31일",
    },
    {
      id: 456,
      unRead: 1,
      participant: {
        name: "어피치",
        profileImage: "https://github.com/shadcn.png",
      },
      description: "귀여웡얼마나길까얼마나길까얼마나길까",
      date: "1월 31일",
    },
    {
      id: 789,
      unRead: 0,
      participant: {
        name: "춘식이",
        profileImage: "https://github.com/shadcn.png",
      },
      description: "귀여웡얼마나길까얼마나길까얼마나길까",
      date: "1월 31일",
    },
    {
      id: 101,
      unRead: 0,
      participant: {
        name: "라이언",
        profileImage: "https://github.com/shadcn.png",
      },
      description: "안녕하세요",
      date: "1월 31일",
    },
    {
      id: 102,
      unRead: 0,
      participant: {
        name: "라이언",
        profileImage: "https://github.com/shadcn.png",
      },
      description: "안녕하세요",
      date: "1월 31일",
    },
    {
      id: 103,
      unRead: 0,
      participant: {
        name: "라이언",
        profileImage: "https://github.com/shadcn.png",
      },
      description: "안녕하세요",
      date: "1월 31일",
    },
    {
      id: 104,
      unRead: 0,
      participant: {
        name: "라이언",
        profileImage: "https://github.com/shadcn.png",
      },
      description: "안녕하세요",
      date: "1월 31일",
    },
  ];
  return <ChatList chatList={CHATLIST} />;
}
