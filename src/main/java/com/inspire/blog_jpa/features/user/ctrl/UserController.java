package com.inspire.blog_jpa.features.user.ctrl;

import com.inspire.blog_jpa.features.user.domain.dto.UserRequestDTO;
import com.inspire.blog_jpa.features.user.domain.dto.UserResponseDTO;
import com.inspire.blog_jpa.features.user.service.UserService;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/*
api 명세서 만들 때 사용하는 네이밍규칙
- 명사로 작성
Get /users
Get /users/{id}
Post /users
Put /users
Delete /users

1.      :         N, 1    :         N
User ---------> Blog ----------> Comment

/user/{email}/blogs
/blogs/{id}/comments
*/


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users")
    public ResponseEntity<?> signUp(@RequestBody UserRequestDTO request) {
        System.out.println(">>>debug user controller sign up" + request );
        try {
            UserResponseDTO user = userService.signUp(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

@PostMapping(value = "/users/signIn", consumes = "application/json")
    public ResponseEntity<?> signIn(
        

        @RequestBody UserRequestDTO request) {
        System.out.println(">>>debug user controller signIn");
        System.out.println(">>> [DEBUG] 도착한 request 객체: " + request); // 여기에 로그 추가!
        System.out.println(">>> [DEBUG] email 값: " + request.getEmail()); // 여기에 로그 추가!

        try {
            Map<String, Object> map = userService.signIn(request);
            //token : access token & refresh token (JWT)
            //token은 header에 심는다.
            HttpHeaders headers = new HttpHeaders();

            headers.add("Authorization","Bearer " + (String)(map.get("at"))); // Access Token
            headers.add("Refresh-Token", (String)(map.get("rt"))); // Refresh Token
            headers.add("Access-Control-Expose-Headers", "Authorization, Refresh-Token");

            return ResponseEntity
                        .status(HttpStatus.OK)
                        .headers(headers)
                        .body((UserResponseDTO)map.get("data"));
        } catch (RuntimeException e) {
            // 로그인 실패(User not found) 시 401 Unauthorized 상태와 오류 메시지를 반환합니다.
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}
