package com.starlightonlinestore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.starlightonlinestore.data.repository")
public class StarlightOnlineStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(StarlightOnlineStoreApplication.class, args);
	}

}
