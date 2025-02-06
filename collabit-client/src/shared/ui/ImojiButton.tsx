import { cn } from "../lib/shadcn/utils";

const ImojiButton = ({
  children,
  value,
  onClick,
  isSelected,
}: {
  children: React.ReactNode;
  value: number;
  onClick: () => void;
  isSelected: boolean;
}) => {
  return (
    <button
      className={cn(
        "h-6 w-6 rounded-lg md:h-9 md:w-9",
        isSelected ? "bg-violet-500" : "bg-violet-200",
      )}
      aria-label={`${value}점`}
      onClick={onClick}
    >
      {children}
    </button>
  );
};
export default ImojiButton;
