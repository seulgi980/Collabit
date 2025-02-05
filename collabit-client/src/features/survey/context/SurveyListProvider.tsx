import { SurveyListResponse } from "@/shared/types/response/survey";
import {
  createContext,
  Dispatch,
  SetStateAction,
  useContext,
  useState,
} from "react";

export const SurveyListContext = createContext<{
  surveyList: SurveyListResponse[];
  setSurveyList: Dispatch<SetStateAction<SurveyListResponse[]>>;
} | null>(null);

export const SurveyListProvider = ({
  children,
  initialData,
}: {
  children: React.ReactNode;
  initialData: SurveyListResponse[];
}) => {
  const [surveyList, setSurveyList] = useState(initialData);
  return (
    <SurveyListContext.Provider value={{ surveyList, setSurveyList }}>
      {children}
    </SurveyListContext.Provider>
  );
};

export const useSurveyList = () => {
  const context = useContext(SurveyListContext);
  if (!context) {
    throw new Error("useSurveyList must be used within a SurveyListProvider");
  }
  return context;
};
