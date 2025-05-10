package com.projetoa3.saudefacil;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class SaudeFacilApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaudeFacilApplication.class, args);
	}

}
