package com.inspire.blog_jpa.features.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "JPA_USER_TBL")

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @Column(nullable = false) 
    private String email  ;
    
    @Column(unique = true, nullable = false , length = 200)
    private String password ;

    private String name ;
    private String role ; 

    // @OneToMany()
    // private List<BlogEntity> blogs = new ArrayList<>(); 

}
