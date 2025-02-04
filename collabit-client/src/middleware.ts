import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";
import { jwtVerify } from "jose";

export async function middleware(request: NextRequest) {
  const token = request.cookies.get("accessToken");
  if (!token) {
    return NextResponse.redirect(new URL("/login", request.url));
  }
  try {
    const secretKey = process.env.NEXT_JWT_SECRET_KEY;
    if (!secretKey) {
      throw new Error("JWT 시크릿 키가 설정되지 않았습니다.");
    }

    const decodedSecret = Buffer.from(secretKey, "base64");
    const secret = new Uint8Array(decodedSecret);

    const { payload } = await jwtVerify(token.value, secret, {
      algorithms: ["HS512"],
    });
    console.log(payload);
    const authorities = payload.auth as string;

    if (!authorities) {
      throw new Error("권한 정보가 없습니다.");
    }
    const requestHeaders = new Headers(request.headers);
    requestHeaders.set("x-user-data", JSON.stringify(payload.auth));
    const response = NextResponse.next();

    return response;
  } catch (error) {
    console.error("JWT 검증 실패:", error);
    return NextResponse.redirect(new URL("/login", request.url));
  }
}

export const config = {
  matcher: [
    "/projects/:path*",
    "/mypage/:path*",
    "/chat/:path*",
    "/report/:path*",
    "/auth/callback",
  ],
};
