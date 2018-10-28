package com.victorseger.svtech;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class SvtechApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SvtechApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
