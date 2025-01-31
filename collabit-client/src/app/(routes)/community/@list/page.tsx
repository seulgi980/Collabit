import { Plus } from "lucide-react";
import Link from "next/link";

const ListPage = () => {
  return (
    <div className="relative w-full">
      <h2 className="sr-only">커뮤니티</h2>
      <div>커뮤니티 리스트</div>
      {/* <ul
        className={cn(
          preview.length > 0 &&
            "grid h-[260px] w-[400px] overflow-hidden rounded-lg",
          preview.length === 1 && "grid-cols-1",
          preview.length === 2 && "grid-cols-2",
          preview.length === 3 && "grid-cols-2",
          preview.length === 4 && "grid-cols-2 grid-rows-2",
        )}
      >
        {preview.map((preview, index) => (
          <li key={preview} className="relative h-full w-full">
            <Image
              className="object-cover"
              src={preview}
              alt={`미리보기 ${index + 1}`}
              fill
              sizes="(max-width: 400px) 100vw"
            />
          </li>
        ))}
      </ul> */}

      <Link
        href={"/community/post"}
        className="fixed bottom-[100px] right-5 inline-flex h-9 w-9 items-center justify-center gap-2 whitespace-nowrap rounded-full bg-violet-700 text-sm font-medium text-primary-foreground shadow transition-colors hover:bg-violet-800 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring disabled:pointer-events-none disabled:opacity-50 md:hidden [&_svg]:pointer-events-none [&_svg]:size-4 [&_svg]:shrink-0"
      >
        <Plus className="text-white" />
      </Link>
    </div>
  );
};

export default ListPage;
