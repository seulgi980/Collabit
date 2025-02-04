"use client";

import { Button } from "@/shared/ui/button";
import { Card } from "@/shared/ui/card";
import { Users, MessageSquareMore, ArrowRight, Github } from "lucide-react";

const NoProjectGuide = () => {
  return (
    <Card className="flex h-[190px] w-full max-w-5xl flex-col items-center justify-center gap-4 bg-gradient-to-br from-violet-50 to-white px-20 py-6">
      <div className="flex flex-col items-center gap-20 md:flex-row">
        <div className="flex items-center gap-2">
          <Users className="h-6 w-6 text-violet-400" />
          <ArrowRight className="h-5 w-5 text-violet-300" />
          <Github className="h-6 w-6 text-violet-400" />
          <ArrowRight className="h-5 w-5 text-violet-300" />
          <MessageSquareMore className="h-6 w-6 text-violet-400" />
        </div>
        <div className="flex flex-col items-center gap-1">
          <p className="text-center text-lg font-semibold">
            프로젝트를 등록하고 팀원들과 소통해보세요
          </p>
          <p className="text-center text-sm text-gray-500">
            프로젝트를 등록하면 협업한 사람들에게 피드백을 받을 수 있어요
          </p>
        </div>
      </div>
      <Button>프로젝트 등록하기</Button>
    </Card>
  );
};

export default NoProjectGuide;
