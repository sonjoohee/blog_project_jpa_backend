package com.inspire.blog_jpa.features.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.inspire.blog_jpa.features.comment.domain.entity.CommentEntity;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer>{
    public List<CommentEntity> findByBlogBlogId(Integer blogId) ; 
}


