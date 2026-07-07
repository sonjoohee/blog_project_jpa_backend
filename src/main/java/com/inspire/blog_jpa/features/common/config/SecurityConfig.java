package com.inspire.blog_jpa.features.common.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.inspire.blog_jpa.features.common.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final JwtAuthenticationFilter jwtAuthenticationFilter ;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder() ; 
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization","Content-Type","Refresh-token"));
        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(true); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source ;
    }

    //
    @Bean 
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println(">>>> debug >>>> SecurityConfig filterChain");
        // http.cors(cors -> cors.configurationSource(corsConfigurationSource()))
        http.cors(Customizer.withDefaults())
            // 1. CSRF(Cross-Site Request Forgery) 보호 기능을 비활성화합니다. JWT 기반의 stateless 인증을 사용하므로 세션을 사용하지 않아 CSRF 공격에 비교적 안전합니다.
            .csrf( csrf -> csrf.disable()) 
            // 2. HTTP 요청에 대한 인가(Authorization) 규칙을 설정합니다.
            .authorizeHttpRequests( auth -> auth
                // 2-1. /swagger-ui/**, /v3/api-docs/**, /api/v1/users 경로는 인증 없이 모든 사용자가 접근할 수 있도록 허용합니다. (주로 API 문서, 회원가입/로그인 경로)
                .requestMatchers("/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/api/v1/users").permitAll()
                // 2-2. 모든 경로에 대한 OPTIONS 메서드 요청을 허용합니다. (CORS Preflight 요청 처리)
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 2-3. /admin/** 패턴의 경로는 "ADMIN" 역할을 가진 사용자만 접근할 수 있도록 제한합니다.
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // 2-4. 위에서 설정한 경로 외의 모든 나머지 요청은 반드시 인증(로그인)된 사용자만 접근할 수 있도록 설정합니다.
                .anyRequest().authenticated()
            // 3. 세션 관리 정책을 STATELESS로 설정합니다. 서버가 클라이언트의 상태를 저장하지 않으므로, 모든 요청은 토큰을 통해 인증됩니다.
            ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) ; 
        // 4. Spring Security의 기본 로그인 필터인 UsernamePasswordAuthenticationFilter 앞에 직접 구현한 jwtAuthenticationFilter를 추가합니다.
        //    이를 통해 모든 요청에 대해 JWT 토큰을 먼저 검증하게 됩니다.
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) ; 
        return http.build() ; 
    }

}
