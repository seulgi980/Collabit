import { cn } from "@/shared/lib/shadcn/utils";
import { Contributor } from "@/shared/types/model/Project";
import { Avatar, AvatarFallback, AvatarImage } from "@radix-ui/react-avatar";

interface ProjectContributorProps {
  contributor: Contributor[];
  size?: "sm" | "md";
}

const ProjectContributor = ({
  contributor,
  size = "md",
}: ProjectContributorProps) => {
  const contributorCount = contributor.length;
  const avatarSize = size === "sm" ? "h-6 w-6" : "h-10 w-10";
  const countSize = size === "sm" ? "text-xs" : "text-sm";
  const margin = size === "sm" ? "ml-[-10px]" : "ml-[-15px]";
  const width = size === "sm" ? "w-[60px]" : "w-[90px]";
  return (
    <div className={cn("flex", width)}>
      {/* 3명 이상일 때 */}
      {contributorCount > 2 ? (
        <>
          <Avatar className={cn("z-20 flex rounded-full bg-black", avatarSize)}>
            <span className={cn("m-auto text-white", countSize)}>
              +{contributorCount - 2}
            </span>
          </Avatar>
          <Avatar className={cn("z-10 rounded-full", margin, avatarSize)}>
            <AvatarImage
              className="rounded-full"
              src={contributor[0].profileImage}
            />
            <AvatarFallback>
              <div className="flex h-full w-full items-center justify-center rounded-full bg-gray-200">
                <span className="text-xs text-gray-500">
                  {contributor[0].githubId.slice(0, 2)}
                </span>
              </div>
            </AvatarFallback>
          </Avatar>
          <Avatar className={cn("z-0 rounded-full", margin, avatarSize)}>
            <AvatarImage
              className="rounded-full"
              src={contributor[1].profileImage}
            />
            <AvatarFallback>
              <div className="flex h-full w-full items-center justify-center rounded-full bg-gray-200">
                <span className="text-xs text-gray-500">
                  {contributor[1].githubId.slice(0, 2)}
                </span>
              </div>
            </AvatarFallback>
          </Avatar>
        </>
      ) : null}

      {/* 2명일 때 */}
      {contributorCount === 2 && (
        <>
          <Avatar className={cn("z-10 rounded-full", avatarSize)}>
            <AvatarImage
              className="rounded-full"
              src={contributor[0].profileImage}
            />
            <AvatarFallback>
              <div className="flex h-full w-full items-center justify-center rounded-full bg-gray-200">
                <span className="text-xs text-gray-500">
                  {contributor[0].githubId.slice(0, 2)}
                </span>
              </div>
            </AvatarFallback>
          </Avatar>
          <Avatar className={cn("z-0 rounded-full", margin, avatarSize)}>
            <AvatarImage
              className="rounded-full"
              src={contributor[1].profileImage}
            />
            <AvatarFallback>
              <div className="flex h-full w-full items-center justify-center rounded-full bg-gray-200">
                <span className="text-xs text-gray-500">
                  {contributor[1].githubId.slice(0, 2)}
                </span>
              </div>
            </AvatarFallback>
          </Avatar>
        </>
      )}

      {/* 1명일 때 */}
      {contributorCount === 1 && (
        <Avatar className={cn("z-10 rounded-full", avatarSize)}>
          <AvatarImage
            className="rounded-full"
            src={contributor[0].profileImage}
          />
          <AvatarFallback>
            <div className="flex h-full w-full items-center justify-center rounded-full bg-gray-200">
              <span className="text-xs text-gray-500">
                {contributor[0].githubId.slice(0, 2)}
              </span>
            </div>
          </AvatarFallback>
        </Avatar>
      )}
    </div>
  );
};

export default ProjectContributor;
