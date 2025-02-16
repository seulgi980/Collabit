import { create } from "zustand";
import { devtools } from "zustand/middleware";

interface NotificationState {
  surveyRequests: number[];
  surveyResponses: number[];
  addSurveyRequests: (ids: number[]) => void;
  addSurveyResponses: (ids: number[]) => void;
}

export const useNotificationStore = create<NotificationState>()(
  devtools(
    (set) => ({
      surveyRequests: [],
      surveyResponses: [],

      addSurveyRequests: (ids) =>
        set(
          () => ({
            surveyRequests: ids,
          }),
          false,
          "notifications/addSurveyRequests",
        ),

      addSurveyResponses: (ids) =>
        set(
          () => ({
            surveyResponses: ids,
          }),
          false,
          "notifications/addSurveyResponses",
        ),
    }),
    {
      name: "Notification Store",
    },
  ),
);
