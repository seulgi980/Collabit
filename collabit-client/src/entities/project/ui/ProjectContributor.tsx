import { Avatar, AvatarFallback, AvatarImage } from "@radix-ui/react-avatar";

interface ProjectContributorProps {
  contributors: Array<{
    githubId: string;
    profileImage: string;
  }>;
}

const ProjectContributor = ({ contributors }: ProjectContributorProps) => {
  const contributorCount = contributors.length;

  return (
    <div className="flex" style={{ width: '90px' }}>
      {/* 3명 이상일 때 */}
      {contributorCount > 2 ? (
        <>
          <Avatar className="z-20 flex h-10 w-10 rounded-full bg-black">
            <span className="m-auto text-white">+{contributorCount - 2}</span>
          </Avatar>
          <Avatar className="z-10 ml-[-15px] h-10 w-10 rounded-full">
            <AvatarImage
              className="rounded-full"
              src={contributors[0].profileImage}
            />
            <AvatarFallback>{contributors[0].githubId}</AvatarFallback>
          </Avatar>
          <Avatar className="z-0 ml-[-15px] h-10 w-10 rounded-full">
            <AvatarImage
              className="rounded-full"
              src={contributors[1].profileImage}
            />
            <AvatarFallback>{contributors[1].githubId}</AvatarFallback>
          </Avatar>
        </>
      ) : null}

      {/* 2명일 때 */}
      {contributorCount === 2 && (
        <>
          <Avatar className="z-10 h-10 w-10 rounded-full">
            <AvatarImage
              className="rounded-full"
              src={contributors[0].profileImage}
            />
            <AvatarFallback>{contributors[0].githubId}</AvatarFallback>
          </Avatar>
          <Avatar className="z-0 ml-[-15px] h-10 w-10 rounded-full">
            <AvatarImage
              className="rounded-full"
              src={contributors[1].profileImage}
            />
            <AvatarFallback>{contributors[1].githubId}</AvatarFallback>
          </Avatar>
        </>
      )}

      {/* 1명일 때 */}
      {contributorCount === 1 && (
        <Avatar className="z-10 h-10 w-10 rounded-full">
          <AvatarImage
            className="rounded-full"
            src={contributors[0].profileImage}
          />
          <AvatarFallback>{contributors[0].githubId}</AvatarFallback>
        </Avatar>
      )}
    </div>
  );
};

export default ProjectContributor;
