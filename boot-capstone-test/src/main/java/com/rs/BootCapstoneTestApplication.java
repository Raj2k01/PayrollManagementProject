package com.rs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class BootCapstoneTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootCapstoneTestApplication.class, args);
	}

}
