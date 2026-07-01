package com.inspire.blog_jpa.features.user.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.inspire.blog_jpa.features.common.util.JwtProvider;
import com.inspire.blog_jpa.features.user.domain.dto.UserRequestDTO;
import com.inspire.blog_jpa.features.user.domain.dto.UserResponseDTO;
import com.inspire.blog_jpa.features.user.domain.entity.UserEntity;
import com.inspire.blog_jpa.features.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    /*
    spring-security : 평문이 아닌 암호화
    jwt :  access token, refresh token(redis, h2 저장) 발급(jwt)
    
    */
    //데이터는 dto로 관리가 되어져 있는데 엔티티로 엔티티는 리스폰스로 변환되어서 반환된다.
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public UserResponseDTO signUp(UserRequestDTO request) {
        System.out.println(">>>debug user service signUp");

        //case 01
        // UserEntity entity = UserRequestDTO.toEntity(request);
        // return UserResponseDTO.fromEntity(userRepository.save(entity));

        // case 02: 가독성과 안정성을 개선
        return Optional.of(request)
                .filter(req -> !userRepository.existsById(req.getEmail()))
                .map(UserRequestDTO::toEntity) // DTO를 Entity로 변환
                .map(userRepository::save)     // Entity를 데이터베이스에 저장
                .map(UserResponseDTO::fromEntity) // 저장된 Entity를 ResponseDTO로 변환
                .orElseThrow(() -> new IllegalStateException("이미 존재하는 이메일입니다: " + request.getEmail()));
    }

    public Map<String, Object> signIn(UserRequestDTO request) {
        System.out.println(">>>debug user service signIn");


        Map<String, Object> map = new HashMap();

        UserEntity entity = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
        .orElseThrow(() -> new RuntimeException("User not found"));

    
        // //provider를 이용해서 token을 생성
        String at = jwtProvider.createAT(request.getEmail());
        String rt = jwtProvider.createRT(request.getEmail());
        System.out.println(">>>debug user service signIn at: " + at);
        System.out.println(">>>debug user service signIn rt: " + rt);

        // //inMemory DB - Redis, H2
        // // at,rt 담아서 관리, 인증코드 - docker 이용(가상화기반에서 redis)

        // //위 3개를 반환하기 가장 좋은것이 map
        map.put("data", UserResponseDTO.fromEntity(entity));
        map.put("at", at);
        map.put("rt", rt);

        return map;
    }   
    
    
}
