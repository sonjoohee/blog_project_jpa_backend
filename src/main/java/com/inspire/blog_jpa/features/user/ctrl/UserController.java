package com.inspire.blog_jpa.features.user.ctrl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inspire.blog_jpa.features.user.domain.dto.UserRequestDTO;
import com.inspire.blog_jpa.features.user.domain.dto.UserResponseDTO;
import com.inspire.blog_jpa.features.user.service.UserService;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


/*
api 명세서 만들 때 사용하는 네이밍규칙 
- Noun 작성
GET    /users
GET    /users/{email}  
POST   /users
PUT    /users/{email}
DELETE /users/{email} 


GET    /api/v1/users
GET    /api/v1/users/{email}  
POST   /api/v1/users
PUT    /api/v1/users/{email}
DELETE /api/v1/users/{email} 


1         :       N , 1      :      N   
User -----------> Blog -----------> Comment

/users/{email}/blogs
/blogs/{id}/comments

*/

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService ;

    //회원가입시 암호를 해싱처리하기 위해서(securityconfig)
    private final PasswordEncoder passwordEncoder ;

    @PostMapping("/users")
    public ResponseEntity<?> signUp(@RequestBody UserRequestDTO request) {
        System.out.println(">>>> debug user controller signUp "); 
        System.out.println(">>>> debug params : "+request); 

        // 패스워드 암호화(해싱) 작업(spring - security) 
        // 1. 유저가 보낸 비밀번호(예: "1234")를 가져와서 encode(분쇄기)로 갈아버림
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        // 2. 이제 request 안의 비밀번호는 외계어("$2a$10$xXyZ...")로 바뀐 상태로 서비스에 넘어감
        UserResponseDTO response = userService.signUp(request) ;

        // status code : 201, 500 
        if(response != null ) {
            return ResponseEntity.status(HttpStatus.CREATED).build() ;     
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build() ; 
        }

    }

    @GetMapping("/users")
    public ResponseEntity<UserResponseDTO> signIn(UserRequestDTO request) {
        System.out.println(">>>> debug user controller signIn "); 
        System.out.println(">>>> debug params : "+request); 

        Map<String, Object> map = userService.signIn(request);


        // token : access token , refresh token
        HttpHeaders headers = new HttpHeaders();
        
        headers.add("Authorization", (String)(map.get("at")));
        headers.add("Refresh-Token", (String)(map.get("rt")));
        headers.add("Access-Control-Expose-Headers", "Authorization, Refresh-Token");
        // service - signIn 

        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body((UserResponseDTO)(map.get("data")));
                
    }

    // @PostMapping("/users/logout")
    // public ResponseEntity<?> signOut(@RequestHeader("Authorization") String authorization) {
    //     System.out.println(">>>> debug user controller logout "); 
    //     System.out.println(">>>> debug user controller logout authorization : "+authorization);

    //     String at = authorization.replace("Bearer ", "");
    //     userService.signOut(at); 

    //     return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        
    // }
    
    
    

}
