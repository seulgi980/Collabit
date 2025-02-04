import { SurveyListResponse } from "../types/response/survey";

const apiUrl = process.env.NEXT_PUBLIC_API_URL;

// 설문 리스트 조회
export const getSurveyListAPI = async (): Promise<SurveyListResponse[]> => {
  const response = await fetch(`${apiUrl}/survey`, {
    headers: { "Content-Type": "application/json" },
    credentials: "include",
  });
  if (!response.ok) {
    throw new Error("설문 리스트 조회 실패");
  }
  return response.json();
};

// 객관식 설문 시작 Get
// survey/question
export const startMultipleSurveyAPI = async () => {
  const response = await fetch(`${apiUrl}/survey/question`, {
    headers: { "Content-Type": "application/json" },
    credentials: "include",
  });
  if (!response.ok) {
    throw new Error("객관식 설문 시작 실패");
  }
  return response.json();
};

// 객관식 설문 답변 전송 post
// survey/{surveyCode}/multiple
export const sendMultipleSurveyAnswerAPI = async (surveyCode: number) => {
  const response = await fetch(`${apiUrl}/survey/${surveyCode}/multiple`, {
    headers: { "Content-Type": "application/json" },
    credentials: "include",
  });
  if (!response.ok) {
    throw new Error("객관식 설문 답변 전송 실패");
  }
  return response.json();
};

// 객관식 설문 조회 Get
//survey/{surveyCode}/multiple
export const getMultipleSurveyAnswerAPI = async (surveyCode: number) => {
  const response = await fetch(`${apiUrl}/survey/${surveyCode}/multiple`, {
    headers: { "Content-Type": "application/json" },
    credentials: "include",
  });
  if (!response.ok) {
    throw new Error("객관식 설문 조회 실패");
  }
  return response.json();
};

// 주관식 설문 시작 post
//survey/{surveyCode}
export const startEssaySurveyAPI = async (surveyCode: number) => {
  const response = await fetch(`${apiUrl}/survey/${surveyCode}`, {
    headers: { "Content-Type": "application/json" },
    credentials: "include",
  });
  if (!response.ok) {
    throw new Error("주관식 설문 시작 실패");
  }
  return response.json();
};

// 주관식 설문 진행 post
//survey/{surveyCode}/essay
export const essaySurveyProgressAPI = async (surveyCode: number) => {
  const response = await fetch(`${apiUrl}/survey/${surveyCode}/essay`, {
    headers: { "Content-Type": "application/json" },
    credentials: "include",
  });
  if (!response.ok) {
    throw new Error("주관식 설문 진행 실패");
  }
  return response.json();
};

// 주관식 설문 답변 조회 Get
//survey/{surveyCode}/essay
export const getEssaySurveyAnswerAPI = async (surveyCode: number) => {
  const response = await fetch(`${apiUrl}/survey/${surveyCode}/essay`, {
    headers: { "Content-Type": "application/json" },
    credentials: "include",
  });
  if (!response.ok) {
    throw new Error("주관식 설문 답변 조회 실패");
  }
  return response.json();
};
