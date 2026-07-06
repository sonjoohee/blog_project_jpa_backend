package com.inspire.blog_jpa.features.blog.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inspire.blog_jpa.features.blog.domain.dto.BlogResponseDTO;
import com.inspire.blog_jpa.features.blog.domain.dto.BlogRequestDTO;
import com.inspire.blog_jpa.features.blog.repository.BlogRepository;
import com.inspire.blog_jpa.features.user.repository.UserRepository;

import com.inspire.blog_jpa.features.blog.domain.entity.BlogEntity;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class BlogService {

    private final UserRepository    userRepository;
    private final BlogRepository    blogRepository;

    // private final CommentRepository    commentRepository;

    public BlogResponseDTO write(BlogRequestDTO request) {
        System.out.println(">>>> debug blog service write "); 

        // spring security add 
        // SecurityContextHolder 
        
        return userRepository.findById(request.getEmail())
            .map(user -> {
                BlogEntity blog = blogRepository.save(
                    request.toEntity(user)
                );

                return BlogResponseDTO.fromEntity(blog);
            })
            .orElseThrow(() -> new RuntimeException("사용자 인증 오류")) ;


    }

    @Transactional(readOnly = true) 
    public List<BlogResponseDTO> blogs() {
        System.out.println(">>>> debug blog service blogs "); 
        return blogRepository.findAll()
                .stream()
                .map(BlogResponseDTO::fromEntity)
                .toList() ;
    }

   


    /*
    bad case)
    blogs 1 : comments 100 
    BlogEntity blog = blogRepository.findById(blogId).get()
    List<CommentEntity> comments = blog.getComments()
    blog.setComments(comments)

    best case)
    Blog  + Comments 한번에 조회하는 것 
    
    */
    @Transactional(readOnly = true)
    public BlogResponseDTO read(Integer blogId) {
        System.out.println(">>>> debug blog service read"); 

        // bad case 
        // blog = blogRepository.findBy()
        // commentRepository.findBlogBlogId(blog) ;
        
        // best case 
        return blogRepository.findByComments(blogId)
                .map(BlogResponseDTO::fromEntityWithComments)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

    }
    
}
