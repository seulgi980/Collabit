import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/shared/ui/dropdown-menu";
import { Ellipsis, LucideIcon } from "lucide-react";

interface MenuOption {
  icon: LucideIcon;
  label: string;
  onClick: (e: React.MouseEvent) => void;
  className?: string;
}

export interface MoreOptionsMenuProps {
  options: MenuOption[];
}

export function MoreOptionsMenu({ options }: MoreOptionsMenuProps) {
  return (
    <DropdownMenu>
      <DropdownMenuTrigger className="flex items-center gap-2 px-2 py-1">
        <Ellipsis
          style={{ width: "1.2rem", height: "1.2rem" }}
          className="text-muted-foreground"
        />
      </DropdownMenuTrigger>
      <DropdownMenuContent>
        {options.map((option, index) => {
          const Icon = option.icon;
          return (
            <DropdownMenuItem
              key={index}
              onClick={option.onClick}
              className={`cursor-pointer ${option.className || ""}`}
            >
              <Icon />
              {option.label}
            </DropdownMenuItem>
          );
        })}
      </DropdownMenuContent>
    </DropdownMenu>
  );
}
