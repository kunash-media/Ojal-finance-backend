package com.ojal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OjalMicroFinanceApplication {

//	Dotenv dotenv = Dotenv.configure().load();
//	dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
	public static void main(String[] args) {
		SpringApplication.run(OjalMicroFinanceApplication.class, args);
	}

}
