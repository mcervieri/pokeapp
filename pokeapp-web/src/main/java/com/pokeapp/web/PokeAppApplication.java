package com.pokeapp.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.pokeapp")

@EntityScan(basePackages = "com.pokeapp.domain")

@EnableJpaRepositories(basePackages = "com.pokeapp.domain")
public class PokeAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(PokeAppApplication.class, args);
    }
}