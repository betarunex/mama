package com.mama.money;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MoneyApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneyApplication.class, args);
	}

}
