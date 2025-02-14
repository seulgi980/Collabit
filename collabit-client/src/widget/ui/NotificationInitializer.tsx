import { notificationService } from "@/shared/service/NotificationService";
import { useEffect } from "react";

const NotificationInitializer = () => {
  useEffect(() => {
    notificationService.connect();
  }, []);
  return null;
};

export default NotificationInitializer;
