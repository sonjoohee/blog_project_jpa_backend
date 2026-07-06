package com.inspire.blog_jpa.features.comment.domain.entity;

import com.inspire.blog_jpa.features.blog.domain.entity.BlogEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "JPA_COMMENT_TBL")

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentId")
    private Integer commentId ;    

    @Column(nullable = false , length = 500) 
    private String comment ;

    @Column(nullable = false , length = 100) 
    private String email; 

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "blogId")
    private BlogEntity blog ;

    public void updateComment(String comment) {
        this.comment = comment ;
    }

}
