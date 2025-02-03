"use client";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/shared/ui/select";
import { cn } from "@/lib/utils";

interface ProjectSortSelectProps {
  sort: string;
  onSort: (value: string) => void;
  className?: string;
}

export default function ProjectSortSelect({
  sort,
  onSort,
  className,
}: ProjectSortSelectProps) {
  return (
    <Select defaultValue={sort} onValueChange={onSort}>
      <SelectTrigger className={cn(className)}>
        <SelectValue placeholder="정렬" />
      </SelectTrigger>
      <SelectContent>
        <SelectItem value="recent">최신순</SelectItem>
        <SelectItem value="old">오래된순</SelectItem>
      </SelectContent>
    </Select>
  );
}
