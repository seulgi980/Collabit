import { create } from "zustand";

interface ModalState {
  isOpen: boolean;
  component: React.ReactNode;
}
interface ModalActions {
  openModal: (component: React.ReactNode) => void;
  closeModal: () => void;
}

const useModalStore = create<ModalState & ModalActions>()((set) => ({
  isOpen: false,
  component: null,
  openModal: (component: React.ReactNode) => set({ isOpen: true, component }),
  closeModal: () => set({ isOpen: false, component: null }),
}));

export default useModalStore;
