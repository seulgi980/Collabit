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
    <>
      {/* 배경 오버레이 추가 */}
      <div
        className="fixed inset-0 z-10 bg-black/50"
        onClick={(e) => {
          e.stopPropagation();
          closeModal();
        }}
      />
      <div
        className="fixed left-1/2 top-1/2 z-20 min-w-[320px] -translate-x-1/2 -translate-y-1/2 transform flex-col items-center justify-center rounded-xl bg-white p-8 shadow-2xl transition-all md:min-w-[440px]"
        role="dialog"
        aria-modal="true"
        aria-labelledby="modal-title"
        onClick={(e) => e.stopPropagation()}
      >
        <div className="flex flex-col items-center justify-center gap-1 px-6">
          <h1 id="modal-title" className="text-xl font-bold text-gray-800">
            {title}
          </h1>
          <p className="text-center text-sm text-gray-600">{description}</p>
        </div>
        <div className="mt-8 flex w-full justify-center gap-4">
          <Button
            type="button"
            className="h-11 w-32 font-medium transition-colors md:h-12 md:w-40"
            onClick={() => {
              handleConfirm();
              closeModal();
            }}
          >
            {confirmText}
          </Button>
          <Button
            type="button"
            className="h-11 w-32 bg-gray-400 font-medium transition-colors hover:bg-gray-500 md:h-12 md:w-40"
            onClick={closeModal}
          >
            {cancelText}
          </Button>
        </div>
      </div>
    </>
  );
};

export default TwoButtonModal;
