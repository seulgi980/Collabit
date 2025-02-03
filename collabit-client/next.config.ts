import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  images: {
    remotePatterns: [
      {
        protocol: "https",
        hostname: "github.com",
      },
      // 다른 이미지 도메인들이 필요한 경우 여기에 추가
    ],
  },
  /* config options here */
};

export default nextConfig;
