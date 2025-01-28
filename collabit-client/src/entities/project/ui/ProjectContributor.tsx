import { Avatar, AvatarFallback, AvatarImage } from "@radix-ui/react-avatar";

interface ProjectContributorProps {
  contributors: Array<{
    githubId: string;
    profileImage: string;
  }>;
}

const ProjectCotnributor = ({ contributors }: ProjectContributorProps) => {
  //이미 사진 들어간 2명 제외시키기
  const contributorCount = contributors.length - 3;
  return (
    <div className="flex">
      <Avatar className="z-20 flex h-10 w-10 rounded-full bg-black">
        <span className="m-auto text-white">+{contributorCount}</span>
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
    </div>
  );
};

export default ProjectCotnributor;
