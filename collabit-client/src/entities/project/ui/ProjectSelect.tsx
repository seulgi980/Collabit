"use client";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/shared/ui/select";

import { Organization } from "@/shared/types/response/project";

import { Avatar, AvatarFallback, AvatarImage } from "@/shared/ui/avatar";

interface ProjectSelectProps {
  options?: Organization[];
  defaultValue: string;
  onValueChange: (value: string) => void;
  disabled?: boolean;
}

export default function ProjectSelect({
  options,
  defaultValue,
  onValueChange,
  disabled,
}: ProjectSelectProps) {
  if (!options || options.length === 0) {
    return (
      <Select disabled>
        <SelectTrigger className="w-1/2">
          <SelectValue placeholder="Loading..." />
        </SelectTrigger>
      </Select>
    );
  }

  return (
    <Select
      value={defaultValue}
      onValueChange={onValueChange}
      disabled={disabled}
    >
      <SelectTrigger className="w-1/2">
        <SelectValue />
      </SelectTrigger>
      <SelectContent>
        {options
          .filter(
            (i) =>
              i &&
              i.organization &&
              typeof i.organization === "string" &&
              i.organization.trim() !== "",
          )
          .map((i) => (
            <SelectItem key={i.organization} value={i.organization}>
              <div className="flex items-center gap-2">
                <Avatar className="h-6 w-6">
                  <AvatarImage src={i.organizationImage} />
                  <AvatarFallback>{i.organization.slice(0, 2)}</AvatarFallback>
                </Avatar>
                {i.organization}
              </div>
            </SelectItem>
          ))}
      </SelectContent>
    </Select>
  );
}
