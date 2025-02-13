import { PostListResponse } from "@/shared/types/response/post";
import { Avatar, AvatarFallback, AvatarImage } from "@/shared/ui/avatar";
import Image from "next/image";
import Link from "next/link";
import { cn } from "@/shared/lib/shadcn/utils";
import formatRelativeTime from "@/shared/utils/formatRelativeTime";
import { CommunityCardMenu } from "@/entities/community/ui/CommunityCardMenu";
import { CommunityCardActions } from "@/entities/community/ui/CommunityCardAction";

const CommunityCard = ({ post }: { post: PostListResponse }) => {
  return (
    <Link
      href={`/community/${post.code}`}
      className="flex w-full flex-col gap-2 border-b border-b-border px-2 py-5"
    >
      <div className="flex w-full items-center justify-between">
        <div className="flex w-full items-center gap-2">
          <Avatar>
            <AvatarImage src={post.author.profileImage} />
            <AvatarFallback>{post.author.nickname.slice(0, 2)}</AvatarFallback>
          </Avatar>
          <span className="text-base font-medium">{post.author.nickname}</span>
          <span className="text-sm text-muted-foreground">
            {formatRelativeTime(post.createdAt.toString())}
          </span>
        </div>
        <CommunityCardMenu post={post} />
      </div>
      <p className="px-2 text-base">{post.content}</p>
      <ul
        className={cn(
          post.images.length > 0 &&
            "mx-2 grid h-[195px] w-full max-w-[350px] overflow-hidden rounded-lg md:h-[260px] md:w-[400px]",
          post.images.length === 1 && "grid-cols-1",
          post.images.length === 2 && "grid-cols-2",
          post.images.length === 3 &&
            "grid-cols-2 grid-rows-2 [&>*:first-child]:row-span-2",
          post.images.length === 4 && "grid-cols-2 grid-rows-2",
        )}
      >
        {post.images.map((image, index) => (
          <li
            key={index}
            className={cn(
              "relative h-full w-full",
              post.images.length === 3 && index > 0 && "h-[130px]",
              post.images.length === 3 && index === 0 && "h-[260px]",
              ((post.images.length >= 2 && index % 2 === 1) ||
                (post.images.length === 3 && index === 2)) &&
                "border-l-2 border-gray-400",
              post.images.length >= 2 &&
                index >= 2 &&
                "border-t-2 border-gray-400",
            )}
          >
            <Image
              className="object-cover"
              src={image}
              alt={`미리보기 ${index + 1}`}
              fill
              sizes="(max-width: 400px) 100vw"
            />
          </li>
        ))}
      </ul>
      <CommunityCardActions post={post} />
    </Link>
  );
};

export default CommunityCard;
