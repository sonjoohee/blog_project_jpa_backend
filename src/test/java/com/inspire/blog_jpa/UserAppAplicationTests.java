package com.inspire.blog_jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.inspire.blog_jpa.features.user.domain.dto.UserRequestDTO;
import com.inspire.blog_jpa.features.user.domain.dto.UserResponseDTO;
import com.inspire.blog_jpa.features.user.domain.entity.UserEntity;
import com.inspire.blog_jpa.features.user.repository.UserRepository;
@SpringBootTest
public class UserAppAplicationTests {

    @Autowired
    private UserRepository userRepository;


    @Test
    public void signUp() {
        System.out.println(">>>debug user app test signUp");
        UserRequestDTO request =  UserRequestDTO.builder()
        .email("test@test.com").password("1234").name("test").build();
        
        UserEntity entity = userRepository.save(UserRequestDTO.toEntity(request));

        
    }


    @Test
    void signIn() {
        System.out.println(">>>debug user app test signIn");
        UserRequestDTO request =  UserRequestDTO.builder()
        .email("star7613@naver.com").password("1111").name("손주희").build();
        
        UserEntity loginEntity = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
        .orElseThrow(() -> new RuntimeException("User not found"));

        UserResponseDTO response = UserResponseDTO.fromEntity(loginEntity);
    }
    
}
