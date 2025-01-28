package com.collabit.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    // http request header 에서 "JWT 토큰이 포함된" 헤더의 이름
    public static final String AUTHORIZATION_HEADER = "Authorization";
    // Authorization header 에서 토큰 앞에 붙는 prefix
    public static final String BEARER_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;


    // JWT 토큰 인증 정보 검증후, SecurityContext 에 검증된 인증 정보 저장
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 1. request header 에서 토큰 추출
        String jwt = resolveToken(request);
        log.debug("JWT token: {}", jwt);

        // 2. validateToken 으로 토큰 유효성 검사
        // 정상 토큰이면 해당 토큰으로 Authentication 을 가져와서 SecurityContext 에 저장
        if(StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            log.debug("JWT token validated");
            Authentication authentication = tokenProvider.getAuthentication(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);

    }

    // Cookie에서 JWT 토큰정보 추출
    private String resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                log.debug("Found cookie: {} = {}", cookie.getName(), cookie.getValue());
                if ("accessToken".equals(cookie.getName())) {
                    log.debug("쿠키에 토큰이 있습니다");
                    return cookie.getValue();
                }
            }
        }
        log.debug("토큰을 찾을 수 없습니다");
        return null;
    }
}
