package com.inspire.blog_jpa.features.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;


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
    private String email;

    @Column( nullable = false, length = 200)
    private String password; 


    private String name;
    private String role;

    // @OneToMany()
    // private List<BlogEntity> blogs = new ArrayList<>();
}
