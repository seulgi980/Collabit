import { FormattedGithubRepo } from "@/shared/types/response/github";
import { Avatar, AvatarImage, AvatarFallback } from "@/shared/ui/avatar";

import Image from "next/image";
import { BookA, Bug, Clock3, GitFork, Github, Star } from "lucide-react";
import { Button } from "@/shared/ui/button";
import { Badge } from "@/shared/ui/badge";
import useGetGithubContributors from "../api/useGetGithubContributors";
import { ScrollArea } from "@/shared/ui/scroll-area";

interface ProjectInfoCardProps {
  repo: FormattedGithubRepo;
}

export function ProjectInfoCard({ repo }: ProjectInfoCardProps) {
  const { contributors, isLoading } = useGetGithubContributors(
    repo.organization,
    repo.title,
  );

  return (
    <ScrollArea className="flex h-full max-h-[calc(100vh-260px)] flex-col gap-4 overflow-y-auto rounded-lg border p-4">
      <div className="flex items-center justify-between gap-4">
        <div className="flex items-center gap-2">
          <Avatar>
            <AvatarImage src={repo.organizationImage} />
            <AvatarFallback>{repo.organization.slice(0, 2)}</AvatarFallback>
          </Avatar>
          <h2 className="text-xl font-bold">{repo.title}</h2>
        </div>
        <Button
          className="bg-black"
          onClick={() => window.open(repo.html_url, "_blank")}
        >
          <Github className="h-4 w-4" />
          Github
        </Button>
      </div>

      <div className="my-2 flex flex-col gap-2">
        <div className="text-md mb-2 flex items-center gap-1.5">
          <BookA className="h-5 w-5" />
          <span>Language: {repo.language || "없음"}</span>
        </div>
        <div className="text-md mb-2 flex items-center gap-1.5">
          <Clock3 className="h-5 w-5" />
          <span>Updated: {new Date(repo.updated_at).toLocaleDateString()}</span>
        </div>
      </div>
      <div className="grid w-full grid-cols-3 gap-2">
        <Badge
          variant="secondary"
          className="flex w-full items-center justify-center gap-2 py-2"
        >
          <Star className="h-5 w-5 fill-yellow-300 text-yellow-300" />
          <span>{repo.stargazers_count}</span>
        </Badge>
        <Badge
          variant="secondary"
          className="flex w-full items-center justify-center gap-2 py-2"
        >
          <GitFork className="h-5 w-5" />
          <span>{repo.forks_count}</span>
        </Badge>
        <Badge
          variant="secondary"
          className="flex w-full items-center justify-center gap-2 py-2"
        >
          <Bug className="h-5 w-5" />
          <span>{repo.open_issues_count}</span>
        </Badge>
      </div>
      <hr className="my-2" />
      <div>
        <h3 className="mb-2 text-lg font-semibold">Contributors</h3>
        <div className="flex flex-wrap gap-2">
          {isLoading ? (
            <p>로딩 중...</p>
          ) : (
            contributors?.map((contributor) => (
              <div
                key={contributor.githubId}
                className="flex items-center gap-2 rounded-lg bg-violet-100 p-2"
              >
                <Image
                  src={contributor.profileImage}
                  alt={contributor.githubId}
                  width={32}
                  height={32}
                  className="rounded-full"
                />
                <span>{contributor.githubId}</span>
              </div>
            ))
          )}
        </div>
      </div>
    </ScrollArea>
  );
}
