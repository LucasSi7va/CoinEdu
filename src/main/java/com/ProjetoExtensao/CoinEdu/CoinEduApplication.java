package com.ProjetoExtensao.CoinEdu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CoinEduApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoinEduApplication.class, args);
	}
}
