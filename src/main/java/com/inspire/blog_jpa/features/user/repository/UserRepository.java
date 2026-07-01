package com.inspire.blog_jpa.features.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inspire.blog_jpa.features.user.domain.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>{
    // 사용자정의 메서드를 추가 
    // findByPropertyName, findByPropertyNameAndPropertyName 
    public Optional<UserEntity> findByEmailAndPassword(String email, String password);    
}


