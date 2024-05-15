package com.janluk.schoolmanagementapp;

import com.janluk.schoolmanagementapp.security.jwk.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class SchoolmanagementappApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolmanagementappApplication.class, args);
	}

}
