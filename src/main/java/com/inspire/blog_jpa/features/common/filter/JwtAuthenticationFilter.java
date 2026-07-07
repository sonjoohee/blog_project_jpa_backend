package com.inspire.blog_jpa.features.common.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{

    @Value("${jwt.secret}") // .yml 에 등록된 키값을 사용할 수 있음.
    private String secret ; 
    private Key key ; 

    @PostConstruct
    private void init() {
        System.out.println("debug >>>> JwtAuthenticationFilter jwt secret : "+secret); 
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)) ;    
    }
    

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        System.out.println("debug >>>> JwtAuthenticationFilter doFilterInternal"); 
        String endPoint = request.getRequestURI() ;
        System.out.println("debug >>>> JwtAuthenticationFilter user request path(endPoint) : "+endPoint) ; 
        String method = request.getMethod() ;
        System.out.println("debug >>>> JwtAuthenticationFilter user request method : "+method) ; 

        // preflight options 로 전달이 이루어질 때만 동작 
        if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            System.out.println("debug >>>> JwtAuthenticationFilter preflight OPTIONS ");
            
            // 환경정보를 SecurityConfing - configuration 통해서 전달 
            // header set : Origin, Method, Header
            // response.setStatus(HttpServletResponse.SC_OK);
            // response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
            // response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
            // response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Refresh-token");
            // response.setHeader("Access-Control-Allow-Credentials", "true");
            

            filterChain.doFilter(request, response);
            
            return ;
        }

        String header = request.getHeader("Authorization");
        System.out.println("debug >>>> JwtAuthenticationFilter header "+header) ; 
        if( header == null || !header.startsWith("Bearer ")) {
            System.out.println("debug >>>> JwtAuthenticationFilter Not Authorization") ; 
            filterChain.doFilter(request, response);
            return ;
        }
        String token = header.substring(7);
        System.out.println("debug >>>> JwtAuthenticationFilter token : "+token) ; 
        System.out.println("debug >>>> JwtFilter token validation ") ; 
        System.out.println("debug >>>> Payload(Claims) == token 추출");

        try{
            // Claims == JWT 데이터(header, payload, sign)    
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
            String email = claims.getSubject() ;
            System.out.println("debug >>>> JwtAuthenticationFilter claims get email : "+email );

            // JwtProvider 의해서 Role 이 입력된 경우 사용가능
            String role = claims.get("role", String.class);
            System.out.println("debug >>>> JwtAuthenticationFilter claims get role : "+role );

            // security 인증정보를 담는객체(Principal, credential, authorities) 
            // UsernamePasswordAuthenticationToken
            // SecurityContextHolder <- user primary key (email) 
            //만약 다음 프로젝트에서는 토큰을 만들 때 role 대신 authority나 type 같은 다른 이름으로 권한 정보를 집어넣었다면, 
            // 이 꺼내는 글자 이름("role")만 매칭되게 슥 바꿔주면 됨.
            UsernamePasswordAuthenticationToken authenticationToken = 
                new UsernamePasswordAuthenticationToken(
                    email, 
                    null, 
                    role != null ?  
                    java.util.List.of(() -> "ROLE_"+role) :
                    java.util.List.of() );
            
            // 요청한 사용자와 인증정보객체 연동         
            authenticationToken
            .setDetails(new WebAuthenticationDetailsSource()
                                .buildDetails(request)); 
            
            // 사용자의 정보를 SecurityContextHolder 저장할 수 있고
            // 필요한 경우 ctrl, service 꺼내서 사용할 수 있음. 
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            System.out.println("debug >>>> JwtAuthenticationFilter context holder set authentication move to ctrl ");
            filterChain.doFilter(request, response);
        }catch(Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return ;
        }

        


    }

    
} 