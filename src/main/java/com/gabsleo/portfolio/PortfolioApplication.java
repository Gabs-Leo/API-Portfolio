package com.gabsleo.portfolio;

import com.gabsleo.portfolio.entitites.AppUser;
import com.gabsleo.portfolio.repositories.AppUserRepository;
import com.gabsleo.portfolio.security.Roles;
import com.gabsleo.portfolio.services.AppUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PortfolioApplication {

	@Value("${env.ADM_USER}")
	private String ADM_USER;

	@Value("${env.ADM_PASSWORD}")
	private String ADM_PASSWORD;

	public static void main(String[] args) {
		SpringApplication.run(PortfolioApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(AppUserService appUserService){
		return args -> {
			if(appUserService.findByEmail(ADM_USER).isEmpty())
				appUserService.register(new AppUser(null, ADM_USER, ADM_PASSWORD, Roles.ADMIN));
		};
	}
}
