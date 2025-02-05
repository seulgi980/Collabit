"use client";
import DotIndicator from "@/entities/common/ui/DotIndicator";
import UserAvatar from "@/entities/common/ui/UserAvatar";
import { Card } from "@/shared/ui/card";
import {
  Carousel,
  CarouselApi,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from "@/shared/ui/carousel";
import { Heart } from "lucide-react";
import Link from "next/link";
import { useState } from "react";

const HotIssueSection = () => {
  const [api, setApi] = useState<CarouselApi>();
  const [current, setCurrent] = useState(0);
  const [count, setCount] = useState(0);

  return (
    <div className="flex w-full flex-col items-center justify-center gap-4 border-b-2 border-gray-200 pb-10 md:gap-8">
      <h3 className="text-lg font-bold md:text-xl">요즘 핫한 소식</h3>

      <div className="flex w-full flex-col items-center justify-center gap-4">
        <Carousel
          setApi={setApi}
          opts={{
            align: "start",
          }}
          className="flex w-full flex-col items-center justify-center gap-4"
        >
          <CarouselContent>
            {Array.from({ length: 5 }).map((_, index) => (
              <CarouselItem key={index} className="md:basis-1/2 lg:basis-1/3">
                <div className="px-2 py-2">
                  <Link href={`/project/${index}`}>
                    <Card className="flex cursor-pointer flex-col justify-between gap-4 px-4 py-6 drop-shadow-lg">
                      <UserAvatar size="sm" user={user} />
                      <p className="text-sm">
                        프로젝트를 열심히 참여하지 않는 사람을 어떻게
                        해야할까요? :(
                      </p>
                      <div className="flex items-center gap-1 text-xs">
                        <div className="flex items-center text-gray-500">
                          <span>5</span>
                          <span>개의 답글</span>
                        </div>
                        <span>·</span>
                        <div className="flex items-center gap-1">
                          <Heart
                            className="fill-red-500 text-red-500"
                            style={{ width: "16px", height: "16px" }}
                          />
                          <span>10</span>
                        </div>
                      </div>
                    </Card>
                  </Link>
                </div>
              </CarouselItem>
            ))}
          </CarouselContent>
          <CarouselPrevious className="hidden md:flex" />
          <CarouselNext className="hidden md:flex" />
        </Carousel>
        <DotIndicator
          current={current}
          count={count}
          api={api}
          setCurrent={setCurrent}
          setCount={setCount}
        />
      </div>
    </div>
  );
};

export default HotIssueSection;
const user = {
  nickname: "김철수",
  githubId: "kimchulsoo",
  profileImage: "https://github.com/kimchulsoo.png",
};
