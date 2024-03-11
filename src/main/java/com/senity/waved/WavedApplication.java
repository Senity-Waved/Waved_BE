package com.senity.waved;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class WavedApplication {

	public static void main(String[] args) {
		SpringApplication.run(WavedApplication.class, args);
	}

}
