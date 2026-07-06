package com.inspire.blog_jpa.features.comment.ctrl;

import com.inspire.blog_jpa.features.comment.repository.CommentRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inspire.blog_jpa.features.blog.domain.dto.BlogResponseDTO;
import com.inspire.blog_jpa.features.comment.domain.dto.CommentRequestDTO;
import com.inspire.blog_jpa.features.comment.domain.dto.CommentResponseDTO;
import com.inspire.blog_jpa.features.comment.service.CommentService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CommentController {

    private final CommentRepository commentRepository;
    private final CommentService commentService ;

    
    @PostMapping("/comments")
    public ResponseEntity<?> write(@RequestBody CommentRequestDTO request) {
        System.out.println(">>>> debug comment controller write "); 
        System.out.println(">>>> debug params : "+request); 

        List<CommentResponseDTO> response = commentService.write(request);
        System.out.println(">>>> debug comment controller write response :  "+response); 
        
        if(response != null ) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response) ;     
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build() ; 
        }
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Integer commentId) {
        System.out.println(">>>> debug comment controller delete "); 
        System.out.println(">>>> debug params comment id : "+commentId); 
        
        commentService.delete(commentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
    }

    @PatchMapping("/comments/{id}")
    public ResponseEntity<?> update(    @PathVariable("id") Integer commentId,
                                        @RequestBody CommentRequestDTO request) {
        System.out.println(">>>> debug comment controller update "); 
        System.out.println(">>>> debug params comment id  : "+commentId); 
        System.out.println(">>>> debug params request dto : "+request); 
      
        commentService.update(commentId, request);
        
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); 
    }
    

}

