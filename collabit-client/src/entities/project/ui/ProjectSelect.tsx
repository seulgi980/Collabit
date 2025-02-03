"use client";
import { Avatar, AvatarFallback, AvatarImage } from "@/shared/ui/avatar";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/shared/ui/select";
import { cn } from "@/lib/utils";
import { Organization } from "@/shared/types/response/project";

interface ProjectSelectProps {
  githubId: string;
  organizations?: Organization[];
  organization: string;
  isLoading: boolean;
  onOrganizationChange: (value: string) => void;
  className?: string;
}

export default function ProjectSelect({
  githubId,
  organizations,
  organization,
  isLoading,
  onOrganizationChange,
  className,
}: ProjectSelectProps) {
  return (
    <Select defaultValue={organization} onValueChange={onOrganizationChange}>
      <SelectTrigger className={cn("w-full", className)}>
        <SelectValue placeholder="조직" />
      </SelectTrigger>
      <SelectContent>
        <SelectItem value={githubId}>
          <div className="flex items-center gap-2">
            <Avatar className="h-6 w-6">
              <AvatarImage src={`https://github.com/${githubId}.png`} />
              <AvatarFallback>{githubId.slice(0, 2)}</AvatarFallback>
            </Avatar>
            {githubId}
          </div>
        </SelectItem>
        {isLoading ? (
          <SelectItem value="loading" disabled>
            로딩 중...
          </SelectItem>
        ) : (
          organizations?.map((org) => (
            <SelectItem key={org.organization} value={org.organization}>
              <div className="flex items-center gap-2">
                <Avatar className="h-6 w-6">
                  <AvatarImage src={org.organizationImage} />
                  <AvatarFallback>
                    {org.organization.slice(0, 2)}
                  </AvatarFallback>
                </Avatar>
                {org.organization}
              </div>
            </SelectItem>
          ))
        )}
      </SelectContent>
    </Select>
  );
}
