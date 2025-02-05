import { cn } from "@/shared/lib/shadcn/utils";
import { CarouselApi } from "@/shared/ui/carousel";
import { useEffect } from "react";
const DotIndicator = ({
  current,
  count,
  api,
  setCurrent,
  setCount,
}: {
  current: number;
  count: number;
  api: CarouselApi;
  setCurrent: (current: number) => void;
  setCount: (count: number) => void;
}) => {
  useEffect(() => {
    if (!api) return;

    const handleSelect = () => {
      setCurrent(api.selectedScrollSnap() + 1);
    };

    setCount(api.scrollSnapList().length);
    setCurrent(api.selectedScrollSnap() + 1);
    api.on("select", handleSelect);

    return () => {
      api.off("select", handleSelect);
    };
  }, [api]);
  return (
    <div className="flex items-center gap-2">
      {Array.from({ length: count }).map((_, index) => (
        <button
          key={index}
          className={cn(
            "rounded-full p-1",
            current === index + 1 ? "bg-violet-500" : "bg-violet-100",
          )}
          onClick={() => {
            api?.scrollTo(index);
          }}
          aria-label={`${index + 1}페이지`}
        />
      ))}
    </div>
  );
};

export default DotIndicator;
