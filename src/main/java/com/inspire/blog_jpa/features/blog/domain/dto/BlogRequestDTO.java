package com.inspire.blog_jpa.features.blog.domain.dto;


import com.inspire.blog_jpa.features.blog.domain.entity.BlogEntity;
import com.inspire.blog_jpa.features.user.domain.dto.UserRequestDTO;
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
public class BlogRequestDTO {

    private String title, content, email;

    public  BlogEntity toEntity(UserEntity request) {
        return BlogEntity.builder()
                .title(this.title)
                .content(this.content)
                .author(request)
                .build();
    }
    
}
