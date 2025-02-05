import { Plus } from "lucide-react";
import Link from "next/link";

const FloatingButton = ({ href }: { href: string }) => {
  return (
    <Link
      href={href}
      className="fixed bottom-[100px] right-5 inline-flex h-9 w-9 items-center justify-center gap-2 whitespace-nowrap rounded-full bg-violet-700 text-sm font-medium text-primary-foreground shadow transition-colors hover:bg-violet-800 focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring disabled:pointer-events-none disabled:opacity-50 md:hidden [&_svg]:pointer-events-none [&_svg]:size-4 [&_svg]:shrink-0"
    >
      <Plus className="text-white" />
    </Link>
  );
};

export default FloatingButton;
