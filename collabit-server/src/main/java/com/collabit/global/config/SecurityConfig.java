package com.collabit.global.config;

import com.collabit.user.handler.OAuth2SuccessHandler;
import com.collabit.user.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;

    public SecurityConfig(CustomOAuth2UserService customOAuth2UserService,
                          OAuth2SuccessHandler oAuth2SuccessHandler) {
        this.customOAuth2UserService = customOAuth2UserService;
        this.oAuth2SuccessHandler = oAuth2SuccessHandler;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable) // csrf 비활성화 (JWT, OAUTH 사용할거라 필요 없음)
            .authorizeHttpRequests(auth -> {
                // 회원가입, 로그인 허용
                auth.requestMatchers("/api/user/sign-up","/api/user/login").permitAll();

                // 그 외 요청은 인증 필요
                auth.anyRequest().authenticated();
            })

            .logout(logout -> logout
                    .logoutUrl("/api/user/logout")
                    // 로그아웃 성공 후 추가작업
                    .logoutSuccessHandler((request, response, authentication) -> {
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.setContentType("application/json");
                        response.getWriter().write("Logout successful! 로그아웃 성공");
                    })
                    .permitAll()
            )

            // Spring Security에서 OAuth2 로그인 기능을 활성화
            .oauth2Login(oauth2 ->  oauth2
                    .userInfoEndpoint(userInfo -> userInfo // 로그인 성공 후 사용자 정보를 가져오는 엔드포인트 설정
                            .userService(customOAuth2UserService) // 사용자 정보 처리할 서비스 설정
                    )
                    .successHandler(oAuth2SuccessHandler)
                    .failureHandler((request, response, exception) -> {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.setContentType("application/json;charset=UTF-8");
                        response.getWriter().write("OAuth 로그인 실패: " + exception.getMessage());
                    })
            )

            // JWT는 세션 기반x -> stateless 방식으로 작동 -> 필요 없는 세션 비활성화
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT 사용 시 세션 비활성화
            );

        return http.build();
    }

    // 비밀번호 암호화 encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
