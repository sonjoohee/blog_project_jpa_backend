package com.inspire.blog_jpa.features.blog.domain.dto;

import java.util.List;

// import com.inspire.lgcns_mybatis.features.comment.domain.dto.CommentResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
//user - blog(1:N) -comment(1:N)
public class BlogResponseDTO {

    private int id;
    private String title, content, email;

    /////////////1:N
    // private List<CommentResponseDTO> comments;
    
}
