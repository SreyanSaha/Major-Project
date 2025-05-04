package com.help.roadhelp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.help")
@EntityScan("com.help.model")
@EnableAutoConfiguration
@EnableJpaRepositories("com.help.repository")
public class RoadhelpApplication {
	public static void main(String[] args) {
		SpringApplication.run(RoadhelpApplication.class, args);
	}
}
