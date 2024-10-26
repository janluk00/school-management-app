package com.janluk.schoolmanagementapp;

import com.janluk.schoolmanagementapp.security.jwk.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
@EnableAsync
public class SchoolmanagementappApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolmanagementappApplication.class, args);
	}

}
