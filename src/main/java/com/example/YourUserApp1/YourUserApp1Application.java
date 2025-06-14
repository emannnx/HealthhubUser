package com.example.YourUserApp1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoAuditing
@EnableMongoRepositories
public class YourUserApp1Application {

	public static void main(String[] args) {
		SpringApplication.run(YourUserApp1Application.class, args);
	}

}
