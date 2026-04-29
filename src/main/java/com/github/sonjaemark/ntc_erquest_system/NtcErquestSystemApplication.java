package com.github.sonjaemark.ntc_erquest_system;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.github.sonjaemark.ntc_erquest_system.dto.RegisterRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.model.Document;
import com.github.sonjaemark.ntc_erquest_system.model.enums.DocumentType;
import com.github.sonjaemark.ntc_erquest_system.model.enums.UserRole;
import com.github.sonjaemark.ntc_erquest_system.repository.DocumentRepository;
import com.github.sonjaemark.ntc_erquest_system.repository.UserModelRepository;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthService;

@SpringBootApplication
public class NtcErquestSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(NtcErquestSystemApplication.class, args);
	}

	@Bean
	public CommandLineRunner run(AuthService authService, 
			UserModelRepository userModelRepository,
			DocumentRepository documentRepository) {
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

			List<Long> studentIds = List.of(3L, 4L, 5L);

			for (Long sId : studentIds) {
				userModelRepository.findById(sId).ifPresent(student -> {
					for (DocumentType type : DocumentType.values()) {
						Document doc = Document.builder()
							.documentType(type)
							.student(student)
							.documentContent("base64_encoded_dummy_content_for_" + type.name().toLowerCase())
							.build();
						
						documentRepository.save(doc);
					}
				});
			}

		};
	}

	// branch rule test 
	// test 2
}
