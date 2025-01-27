const getEnvVar = (key: string) => {
  const value = process.env[key];
  if (!value) {
    throw new Error(`환경변수 ${key}이(가) 설정되지 않았습니다.`);
  }
  return value;
};

export const ENV = {
  API_URL: getEnvVar("NEXT_PUBLIC_API_URL"),
  AWS_S3_BUCKET: getEnvVar("NEXT_AWS_S3_BUCKET"),
  GITHUB_CLIENT_ID: getEnvVar("NEXT_PUBLIC_GITHUB_CLIENT_ID"),
  GITHUB_CLIENT_SECRET: getEnvVar("NEXT_PUBLIC_GITHUB_CLIENT_SECRET"),
} as const;
