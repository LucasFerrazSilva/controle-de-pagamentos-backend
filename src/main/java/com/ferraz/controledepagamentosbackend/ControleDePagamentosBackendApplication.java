package com.ferraz.controledepagamentosbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ControleDePagamentosBackendApplication {


	public static void main(String[] args) {
		SpringApplication.run(ControleDePagamentosBackendApplication.class, args);
	}

}
