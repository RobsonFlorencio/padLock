package com.padlock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class RhprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(RhprojectApplication.class, args);
	}

}
