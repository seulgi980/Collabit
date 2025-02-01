"use client";
import { useAuth } from "@/features/auth/api/useAuth";
import { cn } from "@/shared/lib/shadcn/utils";
import { Avatar, AvatarFallback, AvatarImage } from "@/shared/ui/avatar";
import { Button } from "@/shared/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/shared/ui/dropdown-menu";
import formatRelativeTime from "@/shared/utils/formatRelativeTime";
import {
  DeleteIcon,
  Ellipsis,
  Heart,
  MessageCircle,
  Pencil,
  Send,
} from "lucide-react";
import Image from "next/image";
import Link from "next/link";

interface Post {
  id: number;
  user: {
    nickname: string;
    profileImage: string;
  };
  content: string;
  images: string[];
  likeCount: number;
  commentCount: number;
  isLiked: boolean;
  createdAt: string;
}
const CommunityCard = ({ post }: { post: Post }) => {
  const { userInfo } = useAuth();
  return (
    <Link
      href={`/community/${post.id}`}
      className="flex w-full flex-col gap-2 border-b border-b-border px-2 py-5"
    >
      <div className="flex w-full items-center justify-between">
        <div className="flex w-full items-center gap-2">
          <Avatar>
            <AvatarImage src={post.user.profileImage} />
            <AvatarFallback>{post.user.nickname.slice(0, 2)}</AvatarFallback>
          </Avatar>
          <span className="text-base font-medium">{post.user.nickname}</span>
          <span className="text-sm text-muted-foreground">
            {formatRelativeTime(post.createdAt)}
          </span>
        </div>
        {userInfo?.nickname === post.user.nickname ? (
          <DropdownMenu>
            <DropdownMenuTrigger className="flex items-center gap-2 px-2 py-1">
              <Ellipsis
                style={{ width: "1.2rem", height: "1.2rem" }}
                className="text-muted-foreground"
              />
            </DropdownMenuTrigger>
            <DropdownMenuContent>
              <DropdownMenuItem
                onClick={(e) => {
                  e.stopPropagation();
                }}
                className="cursor-pointer text-red-500"
              >
                <DeleteIcon />
                게시물 삭제
              </DropdownMenuItem>

              <DropdownMenuItem onClick={() => {}} className="cursor-pointer">
                <Pencil />
                게시물 수정
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
        ) : null}
      </div>
      <p className="px-2 text-sm">{post.content}</p>
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
      <div className="flex items-center gap-1 text-muted-foreground">
        <div className="flex items-center gap-1 px-2 py-1">
          <MessageCircle className="size-4" />
          <span className="text-sm">{post.commentCount}</span>
        </div>
        <div className="flex items-center">
          <Button variant="ghost" className="flex items-center px-2 py-1">
            <Heart
              className={cn(
                "size-4",
                post.isLiked && "fill-red-500 text-red-500",
              )}
            />
            <span className="text-sm">{post.likeCount}</span>
          </Button>
        </div>
        <Button variant="ghost" className="flex items-center px-2 py-1">
          <Send className="size-4" />
        </Button>
      </div>
    </Link>
  );
};
export default CommunityCard;
