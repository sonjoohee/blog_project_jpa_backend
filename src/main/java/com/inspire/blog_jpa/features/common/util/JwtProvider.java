package com.inspire.blog_jpa.features.common.util;

import org.springframework.beans.factory.annotation.Value;
import java.nio.charset.StandardCharsets;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
 
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    @Value("${jwt.secret}") //.yml에 등록된 키값을 사용할 수 있음 
    private String secret;

    private final long ACCESSTOKEN_EXPIRATION_TIME = 1000 * 60 *30;
    private final long REFRESHTOKEN_EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;


    private Key getSecretKey() {
        //가져온 secret을 인코딩 시켜주는 작업
        System.out.println("secret key : " + secret);
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    
    //토큰 생성
    public String createAT(String email) {
        System.out.println("createAT : ");
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setSubject(email)
                .setExpiration(new Date(System.currentTimeMillis() + ACCESSTOKEN_EXPIRATION_TIME))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    //리프레시 토큰 생성
    public String createRT(String email) {
        System.out.println("createRT : ");
         return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESHTOKEN_EXPIRATION_TIME))
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    
    }
}
