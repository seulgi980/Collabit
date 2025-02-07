import { cn } from "../lib/shadcn/utils";

interface ImojiButtonProps {
  children: React.ReactNode;
  onClick: () => void;
  isSelected: boolean;
  role?: string;
  "aria-checked"?: boolean;
  "aria-label"?: string;
}

const ImojiButton = ({
  children,
  onClick,
  isSelected,
  role,
  "aria-checked": ariaChecked,
  "aria-label": ariaLabel,
}: ImojiButtonProps) => {
  return (
    <button
      type="button"
      role={role}
      className={cn(
        "h-6 w-6 rounded-lg md:h-9 md:w-9",
        isSelected ? "bg-violet-500" : "bg-violet-200",
      )}
      aria-label={ariaLabel}
      aria-checked={ariaChecked}
      onClick={onClick}
    >
      {children}
    </button>
  );
};
export default ImojiButton;
