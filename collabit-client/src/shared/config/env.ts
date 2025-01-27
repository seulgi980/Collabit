const getEnvVar = (key: string) => {
  const value = process.env[key];
  if (!value) {
    throw new Error(`환경변수 ${key}이(가) 설정되지 않았습니다.`);
  }
  return value;
};

export const ENV = {
  API_URL: getEnvVar("NEXT_PUBLIC_API_URL"),
} as const;
