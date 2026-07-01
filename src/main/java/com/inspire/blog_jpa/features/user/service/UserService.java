package com.inspire.blog_jpa.features.user.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inspire.blog_jpa.features.common.util.JwtProvider;
import com.inspire.blog_jpa.features.user.domain.dto.UserRequestDTO;
import com.inspire.blog_jpa.features.user.domain.dto.UserResponseDTO;
import com.inspire.blog_jpa.features.user.domain.entity.UserEntity;
import com.inspire.blog_jpa.features.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    /*
    spring-security : 평문이 아닌 암호화
    jwt             : access-token, refresh-token(redis, h2 저장) 발급(jwt) 
    */

    private final UserRepository    userRepository ; 
    private final JwtProvider       jwtProvider ;


    @Transactional
    public UserResponseDTO signUp(UserRequestDTO request) {
        System.out.println(">>>> debug user service signUp "); 
        
        // case 01.
        // UserEntity entity = UserRequestDTO.toEntity(request);
        // return UserResponseDTO.fromEntity(userRepository.save(entity)) ; 

        // case 02. 
        return Optional.of(request) 
                .filter(req -> !userRepository.existsById(req.getEmail()))
                .map(req -> userRepository.save(req.toEntity(request)))
                .map(req -> UserResponseDTO.fromEntity(req))
                .orElseThrow(() -> new RuntimeException("User Not Found"));

    }

    public Map<String, Object> signIn(UserRequestDTO request) {
        System.out.println(">>>> debug user service signIn "); 

        Map<String, Object> map = new HashMap<>();
        
        UserEntity entity = 
            userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
            .orElseThrow(() -> 
                new RuntimeException("로그인 실패")
            );
            
        
        String at = jwtProvider.createAT(entity.getEmail());
        String rt = jwtProvider.createRT(entity.getEmail());
        System.out.println(">>>> debug user service at :  "+at);
        System.out.println(">>>> debug user service rt :  "+rt);


        // // inMemory DB - Redis, H2 
        // // at, rt 담아서 관리 , 인증코드 - docker (가상화기반에서 redis)
        map.put("data", UserResponseDTO.fromEntity(entity));
        map.put("at"  , at);
        map.put("rt"  , rt); 

        return map ;

        
    }
}
