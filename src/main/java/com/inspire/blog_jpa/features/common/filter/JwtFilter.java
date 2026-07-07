package com.inspire.blog_jpa.features.common.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/*
Filter ? 
- 특정 endPoint 에 접근할 때 토큰의 유무를 판단

??
상황에 따라서 토큰 유무를 판단하지 않는 endPoint 있다 : white list 

*/
// @Component
public class JwtFilter implements Filter{


    private static final List<String> WHITE_LIST = List.of(
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/api/v1/users"    
    );

    private final AntPathMatcher matcher = new AntPathMatcher();
    public boolean isPath(String path) {
        return WHITE_LIST.stream()
                    .anyMatch( pattern -> matcher.match(pattern, path)) ; 
    }


    @Override
    public void doFilter(   ServletRequest request, 
                            ServletResponse response, 
                            FilterChain chain)
    throws IOException, ServletException {
        System.out.println("debug >>>> JwtFilter doFilter"); 

        HttpServletRequest  req = (HttpServletRequest)request ;
        HttpServletResponse res = (HttpServletResponse)response ;

        String endPoint = req.getRequestURI() ;
        System.out.println("debug >>>> JwtFilter user request path(endPoint) : "+endPoint) ; 
        String method = req.getMethod() ;
        System.out.println("debug >>>> JwtFilter user request method : "+method) ; 

        // preflight options 로 전달이 이루어질 때만 동작 
        if("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            System.out.println("debug >>>> JwtFilter preflight OPTIONS ");
            
            // header set : Origin, Method, Header
            res.setStatus(HttpServletResponse.SC_OK);
            res.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
            res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
            res.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Refresh-token");
            res.setHeader("Access-Control-Allow-Credentials", "true");

            return ;
        }

        if(isPath(endPoint)) {
            System.out.println("debug >>>> JwtFilter "+endPoint+" 는 토큰없이 통과"); 
            chain.doFilter(request, response);
            return ;
        }

        // white list 등록되지않은 endPoint 접근이 발생한다면?
        // request header 에 심어져있는 token 검증(만료, 서명이 맞는지) 
        String header = req.getHeader("Authorization");
        System.out.println("debug >>>> JwtFilter header "+header) ; 
        if( header == null || header.isEmpty()) {
            System.out.println("debug >>>> JwtFilter Not Authorization") ; 
            res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ;
        }
        String token = header;
        System.out.println("debug >>>> JwtFilter token : "+token) ; 
        System.out.println("debug >>>> JwtFilter token validation ") ; 
        try{
            chain.doFilter(request, response); 
        } catch(Exception e) {
            e.printStackTrace();
        }
        



            




    }
    

}
