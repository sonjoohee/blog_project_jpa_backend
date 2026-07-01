package com.inspire.blog_jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class BlogJpaApplication {

	public static void main(String[] args) {
		//env 설정을 스프링에서 .yaml에서 사용 할 수 있도록 자바환경에 추가하는 코드
		Dotenv env = Dotenv.configure().ignoreIfMissing().load();
		env.entries().forEach(entry -> {
			System.setProperty(entry.getKey(), entry.getValue());
		});
		SpringApplication.run(BlogJpaApplication.class, args);
	}

}
