package com.inspire.blog_jpa.features.comment.service;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
    
import com.inspire.blog_jpa.features.blog.domain.dto.BlogRequestDTO;
import com.inspire.blog_jpa.features.blog.domain.dto.BlogResponseDTO;
import com.inspire.blog_jpa.features.blog.domain.entity.BlogEntity;
import com.inspire.blog_jpa.features.blog.repository.BlogRepository;
import com.inspire.blog_jpa.features.comment.domain.dto.CommentRequestDTO;
import com.inspire.blog_jpa.features.comment.domain.dto.CommentResponseDTO;
import com.inspire.blog_jpa.features.comment.domain.entity.CommentEntity;
import com.inspire.blog_jpa.features.comment.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {
    
    private final CommentRepository     commentRepository ;
    private final BlogRepository        blogRepository ;

    @Transactional//. 어느 하나라도 실패하면 전체 과정을 취소하고 안전하게 원래 상태로 되돌리는 역할
    public List<CommentResponseDTO> write(CommentRequestDTO request) {
        System.out.println(">>>> debug comment service write "); 

        //블로그 찾기
        BlogEntity blog = blogRepository.findById(request.getBlogId())
            .orElseThrow(() -> new RuntimeException("Blog Not Found!!"));

            //댓글 저장 (commentRepository.save):
        commentRepository.save(request.toEntity(blog));
        
        // comment.blog_blogId
        // blog.blogId
        //댓글 목록 반환 (commentRepository.findByBlogBlogId):
        //댓글을 하나만 반환하지 않고, 해당 블로그의 모든 댓글을 리스트로 다시 가져옴.
        return commentRepository.findByBlogBlogId(blog.getBlogId()) 
            .stream()
            .map(CommentResponseDTO::fromEntity)
            .toList() ;         
    } 

    @Transactional
    public void delete(Integer commentId) {
        System.out.println(">>>> debug comment service delete "); 
        CommentEntity entity = commentRepository.findById(commentId)
                                .orElseThrow(()-> new RuntimeException(commentId +" Not Exists~~"));

        commentRepository.delete(entity);

        // or 
        // commentRepository.deleteById(commentId);
    }

    /*
    jpa update 주의사항
    - DML : transaction(commit, rollback) - update commit 

    (Dirty Checking)
    Entity entity = repository.findById(commentId)
    entity.xxxxx() ; 수정 // commit 
    */
    @Transactional
    public void update(Integer commentId, CommentRequestDTO request) {
        System.out.println(">>>> debug comment service update "); 
        
        CommentEntity comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new RuntimeException("댓글 찾기 오류"));
        comment.updateComment(request.getComment()); 

    }

    //리팩토링 측면에서 공통의 코드를 메서드로 정의
    private String getAuthEEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
