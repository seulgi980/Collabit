"use client";
import {
  Carousel,
  CarouselApi,
  CarouselContent,
  CarouselItem,
  CarouselNext,
  CarouselPrevious,
} from "@/shared/ui/carousel";
import { useEffect, useState } from "react";
import MainProjectListCard from "../project/ui/MainProjectListCard";

const MyProjectSection = () => {
  const [api, setApi] = useState<CarouselApi>();
  const [current, setCurrent] = useState(0);
  const [count, setCount] = useState(0);

  useEffect(() => {
    if (!api) {
      return;
    }

    setCount(api.scrollSnapList().length);
    setCurrent(api.selectedScrollSnap() + 1);

    api.on("select", () => {
      setCurrent(api.selectedScrollSnap() + 1);
    });
  }, [api]);

  return (
    <div className="flex w-full flex-col items-center justify-center gap-4 border-b-2 border-gray-200 pb-10 md:gap-8">
      <h3 className="text-lg font-bold md:text-xl">나의 프로젝트 소식</h3>

      <Carousel
        setApi={setApi}
        opts={{
          align: "start",
        }}
        className="w-full"
      >
        <CarouselContent>
          {Array.from({ length: 5 }).map((_, index) => (
            <CarouselItem key={index} className="md:basis-1/2 lg:basis-1/3">
              <div className="px-2 py-2">
                <MainProjectListCard project={project} onClick={() => {}} />
              </div>
              {/* <div className="p-1">
                <Card>
                  <CardContent className="flex aspect-square items-center justify-center p-6">
                    <span className="text-3xl font-semibold">{index + 1}</span>
                  </CardContent>
                </Card>
              </div> */}
            </CarouselItem>
          ))}
        </CarouselContent>
        <CarouselPrevious />
        <CarouselNext />
      </Carousel>
    </div>
  );
};

export default MyProjectSection;

const project = {
  title: "네이버 페이 리뉴얼",
  code: 2025,
  participant: 6,
  done: false,
  createdAt: "2024-03-15",
  participationRate: 0.75,
  contributors: [
    {
      githubId: "github1",
      profileImage: `https://www.gravatar.com/avatar/github1?d=identicon&f=y`,
    },
    {
      githubId: "github2",
      profileImage: `https://www.gravatar.com/avatar/github2?d=identicon&f=y`,
    },
    {
      githubId: "johndoe",
      profileImage: `https://www.gravatar.com/avatar/johndoe?d=identicon&f=y`,
    },
    {
      githubId: "janesmith",
      profileImage: `https://www.gravatar.com/avatar/janesmith?d=identicon&f=y`,
    },
    {
      githubId: "developer123",
      profileImage: `https://www.gravatar.com/avatar/developer123?d=identicon&f=y`,
    },
    {
      githubId: "coder456",
      profileImage: `https://www.gravatar.com/avatar/coder456?d=identicon&f=y`,
    },
    {
      githubId: "techie789",
      profileImage: `https://www.gravatar.com/avatar/techie789?d=identicon&f=y`,
    },
    {
      githubId: "webdev101",
      profileImage: `https://www.gravatar.com/avatar/webdev101?d=identicon&f=y`,
    },
  ],
  organization: "organization",
  organizationImage: "organizationImage",
  isUpdated: false,
};
