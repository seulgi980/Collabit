import { useEffect } from "react";
import { notificationService } from "@/shared/service/NotificationService";
import useNotificationStore from "../lib/stores/NotificationStore";

const NotificationHandler = () => {
  const setNotifications = useNotificationStore(
    (state) => state.setNotifications,
  );

  useEffect(() => {
    const unsubscribe = notificationService.subscribe(setNotifications);
    return () => void unsubscribe();
  }, []);

  return null;
};

export default NotificationHandler;
