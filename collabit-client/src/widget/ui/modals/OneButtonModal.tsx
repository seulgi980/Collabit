import useModalStore from "@/shared/lib/stores/modalStore";
import { Button } from "@/shared/ui/button";
import { X } from "lucide-react";

interface OneButtonModalProps {
  title: string;
  description: string;
  buttonText: string;
  handleButtonClick: () => void;
}

const OneButtonModal = ({
  title,
  description,
  buttonText,
  handleButtonClick,
}: OneButtonModalProps) => {
  const closeModal = useModalStore((state) => state.closeModal);
  return (
    <div className="relative flex min-w-[300px] flex-col items-center justify-center gap-4 rounded-lg bg-white px-12 py-8 shadow-xl md:min-w-[400px] md:py-10">
      <Button
        type="button"
        variant="ghost"
        className="absolute right-2 top-2"
        onClick={() => closeModal}
      >
        <X />
      </Button>
      <div className="flex flex-col items-center justify-center gap-2 p-4">
        <h1 className="text-lg font-bold md:text-2xl">{title}</h1>
        <p className="text-xs text-gray-500 md:text-sm">{description}</p>
      </div>
      <Button
        type="button"
        className="h-10 w-full md:h-12"
        onClick={handleButtonClick}
      >
        {buttonText}
      </Button>
    </div>
  );
};

export default OneButtonModal;
