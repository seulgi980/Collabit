import useModalStore from "@/shared/lib/stores/modalStore";
import { Button } from "@/shared/ui/button";
interface TwoButtonModalProps {
  title: string;
  description: string;
  confirmText: string;
  cancelText: string;
  handleConfirm: () => void;
}

const TwoButtonModal = ({
  title,
  description,
  confirmText,
  cancelText,
  handleConfirm,
}: TwoButtonModalProps) => {
  const closeModal = useModalStore((state) => state.closeModal);
  return (
    <div className="fixed z-20 flex min-w-[300px] flex-col items-center justify-center gap-4 rounded-lg bg-white px-12 py-8 shadow-xl md:min-w-[400px] md:py-10">
      <div className="flex flex-col items-center justify-center gap-2 p-4">
        <h1 className="text-lg font-bold md:text-2xl">{title}</h1>
        <p className="text-xs text-gray-500 md:text-sm">{description}</p>
      </div>
      <div className="flex justify-between gap-4">
        <Button
          type="button"
          className="w-30 h-10 md:h-12"
          onClick={handleConfirm}
        >
          {confirmText}
        </Button>
        <Button
          type="button"
          className="w-30 h-10 bg-gray-400 md:h-12"
          onClick={closeModal}
        >
          {cancelText}
        </Button>
      </div>
    </div>
  );
};

export default TwoButtonModal;
