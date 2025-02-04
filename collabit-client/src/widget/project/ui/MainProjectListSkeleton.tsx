const MainProjectListSkeleton = () => {
  return (
    <div className="flex w-full flex-col items-center justify-center gap-4 border-b-2 border-gray-200 pb-10 md:gap-8">
      <div className="grid w-full grid-cols-1 gap-4 px-2 md:grid-cols-2 lg:grid-cols-3">
        {Array.from({ length: 3 }).map((_, index) => (
          <div
            key={index}
            className="flex h-[200px] flex-col gap-4 rounded-lg border p-4"
          >
            <div className="h-6 w-3/4 animate-pulse rounded-md bg-gray-200" />
            <div className="flex items-center gap-2">
              <div className="h-8 w-8 animate-pulse rounded-full bg-gray-200" />
              <div className="h-4 w-24 animate-pulse rounded-md bg-gray-200" />
            </div>
            <div className="flex flex-wrap gap-1">
              {Array.from({ length: 4 }).map((_, i) => (
                <div
                  key={i}
                  className="h-8 w-8 animate-pulse rounded-full bg-gray-200"
                />
              ))}
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default MainProjectListSkeleton;
