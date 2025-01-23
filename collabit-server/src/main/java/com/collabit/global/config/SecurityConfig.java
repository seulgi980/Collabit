package com.collabit.global.config;

import com.collabit.auth.jwt.JwtAccessDeniedHandler;
import com.collabit.auth.jwt.JwtAuthenticationEntryPoint;
import com.collabit.auth.jwt.JwtFilter;
import com.collabit.auth.jwt.TokenProvider;
import com.collabit.user.handler.OAuth2SuccessHandler;
import com.collabit.user.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;


    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable) // csrf 비활성화 (JWT, OAUTH 사용할거라 필요 없음)
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class) // cors필터 추가
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class) // jwt검증 필터 추가

                // exception handling 할 때 우리가 만든 클래스 추가
                .exceptionHandling((exceptionHandling) ->
                        exceptionHandling
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                .accessDeniedHandler(jwtAccessDeniedHandler)
                )

                // JWT는 세션 기반x -> stateless 방식으로 작동 -> 필요 없는 세션 비활성화
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT 사용 시 세션 비활성화
                )

                // 권한 설정
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers("/auth/**").permitAll()
                                .anyRequest().authenticated() // 나머지 요청은 모두 인증 필요
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
            );




        return http.build();
    }

    // 비밀번호 암호화 encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


}
