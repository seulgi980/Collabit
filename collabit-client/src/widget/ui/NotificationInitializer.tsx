"use client";
import { useAuth } from "@/features/auth/api/useAuth";
import { notificationService } from "@/shared/service/NotificationService";
import { useNotificationStore } from "@/shared/lib/stores/NotificationStore";
import { useEffect } from "react";

const NotificationInitializer = () => {
  const { isAuthenticated } = useAuth();
  const { addSurveyRequests, addSurveyResponses } = useNotificationStore();

  useEffect(() => {
    // console.log("NotificationInitializer");
    if (isAuthenticated) {
      // console.log("isAuthenticated");
      // 먼저 이벤트 핸들러 등록
      const unsubscribe = notificationService.subscribe((event) => {
        // console.log(event);

        if (event.type === "newSurveyRequest") {
          // console.log("요청에 대한 전역상태 구독");
          addSurveyRequests(event.data);
        } else if (event.type === "newSurveyResponse") {
          // console.log("응답에 대한 전역상태 구독");
          addSurveyResponses(event.data);
        }
      });

      // 그 다음 연결 시작 (초기 알림 포함)
      notificationService.connect();

      return () => {
        unsubscribe();
        notificationService.disconnect();
      };
    }
  }, [isAuthenticated, addSurveyRequests, addSurveyResponses]);

  return null;
};

export default NotificationInitializer;
