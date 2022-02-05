package com.mailparser.mailextract;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MailextractApplication {

	public static void main(String[] args) {
		SpringApplication.run(MailextractApplication.class, args);
	}

}
