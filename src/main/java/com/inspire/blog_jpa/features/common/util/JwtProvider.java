package com.inspire.blog_jpa.features.common.util;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtProvider {

    @Value("${jwt.secret}") // .yml 에 등록된 키값을 사용할 수 있음.
    private String secret ; 


    // private final long ACCESS_TOKEN_EXPIRY  = 1000L * 60 * 30 ; 
    // private final long REFRESH_TOKEN_EXPIRY = 1000L * 60 * 60 * 24 * 7 ;  


    private Key getSecretKey() {
        System.out.println("debug >>>> Provider jwt secret : "+secret); 
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)) ;    
    }
    
    public String createAT(String email) {
        System.out.println("debug >>>> Provider createAT : ");

        return Jwts.builder()
                    .setSubject(email)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 30))
                    .signWith(getSecretKey())
                    .compact() ;
    }

    public String createRT(String email) {
        System.out.println("debug >>>> Provider createRT : ");

        return Jwts.builder()
                    .setSubject(email)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7 ))
                    .signWith(getSecretKey())
                    .compact() ;
    }

    //access token를 통해서 subject 추출
    public String getUserEmailFromAT(String at) {
        System.out.println("debug >>>> Provider getUserEmailFromAT at : "+at); 
        Claims claims = Jwts.parser()
                .setSigningKey(getSecretKey())
                .parseClaimsJws(at)
                .getBody() ; 
        System.out.println("debug >>>> Provider claims.getSubject : "+claims.getSubject());         
        return claims.getSubject() ;     
    }
}
