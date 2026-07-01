package com.inspire.blog_jpa.features.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


// jwt version 
@Configuration
public class CorsConfig implements WebMvcConfigurer { 
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("*")
                .allowedOriginPatterns("http://localhost:3000")
                .allowedMethods("GET","POST","DELETE","PUT","OPTIONS", "PATCH") ; 
                
    }
}

