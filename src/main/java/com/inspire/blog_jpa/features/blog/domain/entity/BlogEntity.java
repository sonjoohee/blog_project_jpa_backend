package com.inspire.blog_jpa.features.blog.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.inspire.blog_jpa.features.comment.domain.entity.CommentEntity;
import com.inspire.blog_jpa.features.user.domain.entity.UserEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;



@Entity
@Table(name = "JPA_BLOG_TBL")

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor

//참조관계 엔티티는 엔티티 간의 연관 관계를 관리해야함
//cascade, fetch, optional, orphanRemoval
public class BlogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto incrementß
    private Integer blogId;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 200)
    private String content;


    //외래키
    //select * from user where id =x ; blog 조회하지 않음
    //fetch : FetchType.LAZY : 필요할 때만 fetching
    //optional : null 허용 여부, false : null 허용X
    @ManyToOne(fetch = FetchType.LAZY, optional = false) 
    @JoinColumn(name = "email")
    private UserEntity author;

    
    // 댓글 Comment
    // 연관댓글 삭제 x: @OneToMany(mappedBy = "blog", orphanRemoval = false)
    // 연관댓글 삭제 o: @OneToMany(mappedBy = "blog", cascade = xxxx.ALL, orphanRemoval = false)
    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments = new ArrayList<>();


    
}
