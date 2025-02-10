import { SurveyDetailResponse } from "@/shared/types/response/survey";
import { create } from "zustand";
import { devtools } from "zustand/middleware";

interface SurveyState {
  id: number | null;
  surveyDetail: SurveyDetailResponse | null;
  multipleAnswers: number[];
}

interface SurveyActions {
  setId: (id: number) => void;
  setSurveyDetail: (surveyDetail: SurveyDetailResponse) => void;
  setMultipleAnswers: (score: number, index: number) => void;
}

export const useSurveyStore = create<SurveyState & SurveyActions>()(
  devtools(
    (set) => ({
      id: null,
      surveyDetail: null,
      multipleAnswers: [],
      setId: (id) => set({ id }, false, "setId"),
      setSurveyDetail: (surveyDetail) =>
        set({ surveyDetail }, false, "setSurveyDetail"),
      setMultipleAnswers: (score: number, index: number) =>
        set(
          (state) => {
            const answers = [...state.multipleAnswers];
            answers[index] = score;
            return { multipleAnswers: answers };
          },
          false,
          "setMultipleAnswers",
        ),
    }),
    {
      name: "Survey Store",
    },
  ),
);
