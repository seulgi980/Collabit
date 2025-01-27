"use client";
import useModalStore from "@/shared/lib/stores/modalStore";

const ModalContainer = () => {
  const isOpen = useModalStore((state) => state.isOpen);
  const component = useModalStore((state) => state.component);

  if (!isOpen) return null;
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50">
      {component}
    </div>
  );
};

export default ModalContainer;
