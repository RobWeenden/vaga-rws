package com.vagarws;

import java.util.UUID;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.vagarws")
public class VagaRwsApplication {

	public static void main(String[] args) {
		SpringApplication.run(VagaRwsApplication.class, args);
		System.out.println(UUID.randomUUID());
	}

}
