package com.inspire.blog_jpa.features.user.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inspire.blog_jpa.features.user.domain.entity.UserEntity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true) // 이걸 추가!

/*

Validation Annotation
@NotNull
@NotEmpty
@NotBlank
@Pattern(정규표현식)
@Size etc ...
*/
public class UserRequestDTO {

    @Email(message = "이메일 형식에 맞지 않습니다.")
    @NotNull(message = "이메일은 필수 입력 값입니다.")
    private String email;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Size(min = 4,max = 20, message = "비밀번호는 4자 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    private String role;

    //jpa는 xxxxEntity 기반으로 동작하므로 DTO -> Entity
    public static UserEntity toEntity(UserRequestDTO request) {
        return UserEntity.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .name(request.getName())
                .role(request.getRole())
                .build();
    }
}


