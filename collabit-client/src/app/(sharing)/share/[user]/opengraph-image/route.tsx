import { ImageResponse } from "next/og";
import { NextRequest } from "next/server";
import { getPortfolioShareAPI } from "@/shared/api/report";

// Edge Runtime으로 변경
export const runtime = "edge";

export async function GET(request: NextRequest) {
  try {
    const pathParts = request.nextUrl.pathname.split("/");
    const user = pathParts[pathParts.length - 2];

    if (!user) {
      throw new Error("User ID not found in URL");
    }

    const data = await getPortfolioShareAPI(user);
    console.log("User:", user);
    console.log("Data received:", JSON.stringify(data, null, 2));

    // Edge Runtime에서는 fs 모듈을 사용할 수 없으므로, 로고는 URL로 변경
    const logoUrl = new URL("/images/logo-lg.png", request.nextUrl.origin).href;

    const strength = data?.aiSummary?.strength ?? "협업 역량을 분석중입니다.";
    const nickname = data?.portfolioInfo?.nickname ?? user;

    return new ImageResponse(
      (
        <div
          style={{
            background: "linear-gradient(to bottom, #1E1E1E, #2E1E3E)",
            width: "100%",
            height: "100%",
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            justifyContent: "center",
            padding: "60px",
            fontFamily: "system-ui",
          }}
        >
          <div
            style={{
              position: "absolute",
              top: 40,
              left: 40,
              display: "flex",
              alignItems: "center",
              gap: "12px",
            }}
          >
            <img src={logoUrl} width="32" height="32" alt="Collabit Logo" />
            <span
              style={{
                color: "#ffffff",
                fontSize: 24,
                display: "block",
              }}
            >
              Collabit
            </span>
          </div>

          <div
            style={{
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              gap: "24px",
              maxWidth: "800px",
            }}
          >
            <div
              style={{
                fontSize: 48,
                fontWeight: "bold",
                color: "#ffffff",
                textAlign: "center",
                display: "flex",
              }}
            >
              {nickname}님의 강점
            </div>

            <div
              style={{
                fontSize: 32,
                color: "#E2E8F0",
                textAlign: "center",
                lineHeight: 1.4,
                padding: "24px",
                background: "rgba(255, 255, 255, 0.1)",
                borderRadius: "16px",
                border: "1px solid rgba(255, 255, 255, 0.2)",
                display: "flex",
              }}
            >
              {strength}
            </div>

            <div
              style={{
                position: "absolute",
                bottom: 40,
                color: "#94A3B8",
                fontSize: 24,
                display: "flex",
              }}
            >
              Collabit AI가 분석한 협업 리포트
            </div>
          </div>
        </div>
      ),
      {
        width: 1200,
        height: 630,
      },
    );
  } catch (error) {
    console.error("Error details:", error);

    return new ImageResponse(
      (
        <div
          style={{
            background: "linear-gradient(to bottom, #1E1E1E, #2E1E3E)",
            width: "100%",
            height: "100%",
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            fontSize: 32,
            color: "white",
            fontFamily: "system-ui",
          }}
        >
          Collabit 협업 리포트
        </div>
      ),
      {
        width: 1200,
        height: 630,
      },
    );
  }
}
