import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import { ReactQueryDevtools } from "@tanstack/react-query-devtools";
import QueryProviders from "../shared/lib/query/QueryProviders";

const inter = Inter({
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "Collabit",
  description:
    "동료들의 솔직한 피드백으로 발견하는 나의 숨겨진 협업 스타일. IT 업계 종사자들을 위한 새로운 성장 플랫폼, collabit",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko-KR">
      <body className={`${inter.className}`}>
        <QueryProviders>
          {children}
          <ReactQueryDevtools />
        </QueryProviders>
      </body>
    </html>
  );
}
