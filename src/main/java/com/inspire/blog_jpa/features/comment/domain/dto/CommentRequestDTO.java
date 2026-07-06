package com.inspire.blog_jpa.features.comment.domain.dto;

import com.inspire.blog_jpa.features.blog.domain.entity.BlogEntity;
import com.inspire.blog_jpa.features.comment.domain.entity.CommentEntity;
import com.inspire.blog_jpa.features.user.domain.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDTO {

    private String  comment ;
    
    private String  email ;

    private Integer blogId ;

    public CommentEntity toEntity(BlogEntity request) {
        return CommentEntity.builder()
                    .comment(this.comment)
                    .email(this.email)
                    .blog(request)
                    .build() ;
    }

}
