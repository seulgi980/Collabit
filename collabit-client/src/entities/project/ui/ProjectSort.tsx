"use client";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/shared/ui/select";

interface ProjectSortSelectProps {
  sort: "asc" | "desc";
  onSort: (value: "asc" | "desc") => void;
}

export default function ProjectSortSelect({
  sort,
  onSort,
}: ProjectSortSelectProps) {
  return (
    <Select defaultValue={sort} onValueChange={onSort}>
      <SelectTrigger className="w-1/2">
        <SelectValue placeholder="정렬" />
      </SelectTrigger>
      <SelectContent>
        <SelectItem value="desc">최신순</SelectItem>
        <SelectItem value="asc">오래된순</SelectItem>
      </SelectContent>
    </Select>
  );
}
