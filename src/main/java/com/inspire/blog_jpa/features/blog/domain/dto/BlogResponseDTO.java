package com.inspire.blog_jpa.features.blog.domain.dto;
import java.util.List;

import com.inspire.blog_jpa.features.blog.domain.entity.BlogEntity;
import com.inspire.blog_jpa.features.comment.domain.dto.CommentResponseDTO;
import com.inspire.blog_jpa.features.user.domain.dto.UserResponseDTO;
import com.inspire.blog_jpa.features.user.domain.entity.UserEntity;

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

    private Integer blogId;
    private String title, content, email;
    

    //블로그 한번 불러오면  댓글도 같이 가져온다.
    private List<CommentResponseDTO> comments;

   // 단순하게 blog 반환하는 구조(write, update, list(BlogDTO)
    public static BlogResponseDTO fromEntity(BlogEntity entity) {
        return BlogResponseDTO.builder()
                .blogId(entity.getBlogId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .email(entity.getAuthor().getEmail())
                .build();
    }

   //blog+ comment(N+1) 반환하는 구조(read)

     public static BlogResponseDTO fromEntityWithComments(BlogEntity entity) {
        return BlogResponseDTO.builder()
                .blogId(entity.getBlogId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .email(entity.getAuthor().getEmail())
                .comments(entity.getComments()
                        .stream()
                        .map(CommentResponseDTO::fromEntity)
                        .toList())
                .build();
    
    
}
}
