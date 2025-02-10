import { SurveyDetailResponse } from "@/shared/api/survey";
import { create } from "zustand";
import { devtools } from "zustand/middleware";

interface SurveyState {
  id: number | null;
  surveyDetail: SurveyDetailResponse | null;
  scores: number[];
}

interface SurveyActions {
  setId: (id: number) => void;
  setSurveyDetail: (surveyDetail: SurveyDetailResponse) => void;
  setScores: (score: number, index: number) => void;
}

export const useSurveyStore = create<SurveyState & SurveyActions>()(
  devtools(
    (set) => ({
      id: null,
      surveyDetail: null,
      scores: [],
      setId: (id) => set({ id }, false, "setId"),
      setSurveyDetail: (surveyDetail) =>
        set({ surveyDetail }, false, "setSurveyDetail"),
      setScores: (score: number, index: number) =>
        set(
          (state) => {
            const newScores = [...state.scores];
            newScores[index] = score;
            return { scores: newScores };
          },
          false,
          "setScores",
        ),
    }),
    {
      name: "Survey Store",
    },
  ),
);
