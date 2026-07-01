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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


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

    @PostMapping("/users")
    public ResponseEntity<?> signUp(@RequestBody UserRequestDTO request) {
        System.out.println(">>>> debug user controller signUp "); 
        System.out.println(">>>> debug params : "+request); 

        // 패스워드 암호화 작업(spring - security) 
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
    
    

}
