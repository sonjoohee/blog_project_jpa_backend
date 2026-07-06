package com.inspire.blog_jpa.features.blog.repository;

import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.inspire.blog_jpa.features.blog.domain.entity.BlogEntity;

@Repository
public interface BlogRepository extends JpaRepository<BlogEntity, Integer>{
    // JPQL
    @Query("""
        SELECT      b
        FROM        BlogEntity b        
        LEFT JOIN   FETCH b.comments
        WHERE       b.blogId = :blogId 
    """)
    public Optional<BlogEntity> findByComments(@Param("blogId") Integer blogId) ;

}


