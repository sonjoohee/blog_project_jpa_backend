package com.inspire.blog_jpa.features.common.filter;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.Filter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/*
filter
- 특정 endPoint 에 접근할 때 토큰의 유무를 판단
- 사용자 요청은 필터를 거친다, 필터를 통해서 controller로 이동

- 상황에 따라서 토큰 유무를 판단하지 않는 endPoint가 있다: whiteList

*/
@Component
public class JwtFilter implements Filter {

    @Value("${jwt.secret}") //.yml에 등록된 키값을 사용할 수 있음 
    private String secret;
    private Key key;



    @PostConstruct
    private void init() {
        //가져온 secret을 인코딩 시켜주는 작업
        System.out.println("secret key : " + secret);
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private static final List<String> WHITE_LIST = List.of(
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api/v1/users", // POST (회원가입)
            "/api/v1/users/signIn" // POST (로그인)
    );
    
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    public boolean isPath(String path) {
        // Use the AntPathMatcher for accurate pattern matching.
        return WHITE_LIST.stream().anyMatch(pattern -> antPathMatcher.match(pattern, path));
    }

    @Override
    public void doFilter(ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
                System.out.println("JwtFilter");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String endPoint = httpRequest.getRequestURI();
        // 이 로그를 추가해서 실제 어떤 경로로 들어오는지 정확히 확인하자!
        System.out.println("endPoint : " + endPoint);
        String method = httpRequest.getMethod();
        System.out.println("method : " + method);

        //preFlight options 로 전달이 이루어질때만 실행
        if("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())){
            System.out.println("debug : jwtfilter preFlight OPTIONS");

            //header set:  Origin, Method, Headers
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
            httpResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,PATCH,DELETE,OPTIONS");
            httpResponse.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");

            chain.doFilter(request, response); //ctrl로 이동
            return;
        }

        if (isPath(endPoint)) {
            System.out.println("debug : jwtfilter 토큰 없이 통과");
            chain.doFilter(request, response); //ctrl로 이동
            return;
        }

        //white list 등록되지 않은 endPoint 접근이 발생한다면?
        //request header에 심어져 있는 token검증(만료, 서명이 맞는지)
        String header = httpRequest.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            System.out.println("debug : jwtfilter 토큰 없이 접근");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String token = header.substring(7);
        System.out.println("token : " + token);
        System.out.println("debug : jwtfilter token validation");
        System.out.println("debug : payload(claims) == token 추출");


        try {      
            Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token);

            System.out.println("debug : jwtfilter token validation success move to contorller");

            
            chain.doFilter(request, response);
        } catch (Exception e) {
            System.out.println("debug : jwtfilter token validation fail");
          e.printStackTrace();
        }

   
    }
    
}
