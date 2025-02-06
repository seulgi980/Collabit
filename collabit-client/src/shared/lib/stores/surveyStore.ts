import { SurveyDetailResponse } from "@/shared/api/survey";
import { create } from "zustand";
import { devtools } from "zustand/middleware";

interface SurveyState {
  id: number | null;
  surveyDetail: SurveyDetailResponse | null;
}

interface SurveyActions {
  setId: (id: number) => void;
  setSurveyDetail: (surveyDetail: SurveyDetailResponse) => void;
}

export const useSurveyStore = create<SurveyState & SurveyActions>()(
  devtools(
    (set) => ({
      id: null,
      surveyDetail: null,
      setId: (id) => set({ id }, false, "setId"),
      setSurveyDetail: (surveyDetail) =>
        set({ surveyDetail }, false, "setSurveyDetail"),
    }),
    {
      name: "Survey Store",
    },
  ),
);
