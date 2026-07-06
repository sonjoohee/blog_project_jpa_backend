
package com.inspire.blog_jpa.features.comment.domain.dto;

import com.inspire.blog_jpa.features.comment.domain.entity.CommentEntity;

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
public class CommentResponseDTO {
    private Integer commentId ;
    private String  comment ; 
    private String  email ;
    private Integer blogId ;

    public static CommentResponseDTO fromEntity(CommentEntity entity) {
        return CommentResponseDTO.builder()
                .commentId(entity.getCommentId())
                .comment(entity.getComment())
                .email(entity.getEmail())
                .blogId(entity.getBlog().getBlogId())
                .build();
    }
}
