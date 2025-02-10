const apiUrl = process.env.NEXT_PUBLIC_API_URL;

const fetchOptions = {
  credentials: "include" as RequestCredentials,
  headers: {
    "Content-Type": "application/json",
  },
};

export const getPortfolioStatus = async () => {
  try {
    const res = await fetch(`${apiUrl}/portfolio`, {
      method: "GET",
      ...fetchOptions,
    });
    if (!res.ok) {
      throw new Error("포트폴리오 상태 조회회에 실패했습니다.");
    }
    return;
  } catch (error) {
    console.error(error);
    throw error;
  }
};

export const getPortfolioInfo = async () => {
  try {
    const res = await fetch(`${apiUrl}/portfolio/info`, {
      method: "GET",
      ...fetchOptions,
    });

    if (!res.ok) {
      throw new Error("포트폴리오 정보 조회에 실패했습니다.");
    }
    return;
  } catch (error) {
    console.error(error);
    throw error;
  }
};

export const checkReportRefresh = async () => {

  try {
    const res = await fetch(`${apiUrl}/portfolio`, {
      method: "GET",
      ...fetchOptions,
    });
    if (!res.ok) {
      throw new Error("포트폴리오 갱신 가능 여부 조회에 실패했습니다.");
    }
    return;
  } catch (error) {
    console.error(error);
    throw error;
  }
};
