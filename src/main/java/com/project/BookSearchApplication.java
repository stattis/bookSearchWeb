package com.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.project.mapper")
public class BookSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookSearchApplication.class, args);
	}

}
