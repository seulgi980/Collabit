import { create } from "zustand";

interface NotificationStates {
  notifications: any[];
}
interface NotificationActions {
  setNotifications: (notifications: any[]) => void;
}

const useNotificationStore = create<NotificationStates & NotificationActions>()(
  (set) => ({
    notifications: [],
    setNotifications: (notifications) => set({ notifications }),
  }),
);

export default useNotificationStore;
