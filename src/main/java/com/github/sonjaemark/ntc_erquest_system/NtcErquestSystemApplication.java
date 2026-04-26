package com.github.sonjaemark.ntc_erquest_system;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.github.sonjaemark.ntc_erquest_system.dto.RegisterRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.model.enums.UserRole;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthService;

@SpringBootApplication
public class NtcErquestSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(NtcErquestSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(AuthService authService) {
		return args -> {
			// Registrar 1
			authService.register(
				new RegisterRequestDTO(
					"registrar1@email.com", 
					"password123", 
					"password123", 
					"Alice",
					"Smith", 
					UserRole.REGISTRAR)
			);

			// Registrar 2
			authService.register(
				new RegisterRequestDTO(
					"registrar2@email.com", 
					"password123", 
					"password123", 
					"Bob",
					"Jones", 
					UserRole.REGISTRAR)
			);

			// Student 1
			authService.register(
				new RegisterRequestDTO(
					"student1@email.com", 
					"password123", 
					"password123", 
					"Charlie",
					"Brown", 
					UserRole.STUDENT)
			);

			// Student 2
			authService.register(
				new RegisterRequestDTO(
					"student2@email.com", 
					"password123", 
					"password123", 
					"Diana",
					"Prince", 
					UserRole.STUDENT)
			);

			// Student 3
			authService.register(
				new RegisterRequestDTO(
					"student3@email.com", 
					"password123", 
					"password123", 
					"Edward",
					"Norton", 
					UserRole.STUDENT)
			);

		};
	}

	// branch rule test 
	// test 2
}
