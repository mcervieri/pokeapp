package com.pokeapp.web.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Profile("!test")
@EntityScan(basePackages = "com.pokeapp.domain")
@EnableJpaRepositories(basePackages = "com.pokeapp.application.repository")
public class JpaConfig {
}