package com.github.sonjaemark.ntc_erquest_system;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.github.sonjaemark.ntc_erquest_system.dto.RegisterRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.model.Document;
import com.github.sonjaemark.ntc_erquest_system.model.DocumentRequest;
import com.github.sonjaemark.ntc_erquest_system.model.Payment;
import com.github.sonjaemark.ntc_erquest_system.model.enums.DocumentType;
import com.github.sonjaemark.ntc_erquest_system.model.enums.PaymentMethod;
import com.github.sonjaemark.ntc_erquest_system.model.enums.Purpose;
import com.github.sonjaemark.ntc_erquest_system.model.enums.RequestStatus;
import com.github.sonjaemark.ntc_erquest_system.model.enums.UserRole;
import com.github.sonjaemark.ntc_erquest_system.repository.DocumentRepository;
import com.github.sonjaemark.ntc_erquest_system.repository.DocumentRequestRepository;
import com.github.sonjaemark.ntc_erquest_system.repository.PaymentRepository;
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
			DocumentRepository documentRepository,
		    DocumentRequestRepository documentRequestRepository,
		    PaymentRepository paymentRepository) {
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

			documentRequestRepository.saveAll(List.of(
				DocumentRequest.builder()
					.purpose(Purpose.SCHOLARSHIP)
					.documentType(DocumentType.CERTIFICATE_OF_ENROLLMENT)
					.document(documentRepository.findById(1L).orElseThrow())
					.additionalDetails("Need for personal use")
					.remarks("Please process quickly")
					.status(RequestStatus.PENDING)
					.student(userModelRepository.findById(3L).orElseThrow())
					.registrar(userModelRepository.findById(1L).orElseThrow())
					.build(),
				DocumentRequest.builder()
					.purpose(Purpose.SCHOLARSHIP)
					.documentType(DocumentType.CERTIFICATE_OF_ENROLLMENT)
					.document(documentRepository.findById(2L).orElseThrow())
					.additionalDetails("Required for job application")
					.remarks("Urgent request")
					.status(RequestStatus.PENDING)
					.student(userModelRepository.findById(4L).orElseThrow())
					.registrar(userModelRepository.findById(1L).orElseThrow())
					.build(),
				DocumentRequest.builder()
					.purpose(Purpose.SCHOLARSHIP)
					.documentType(DocumentType.DIPLOMA)
					.document(documentRepository.findById(3L).orElseThrow())
					.additionalDetails("For graduate school application")
					.remarks("Please expedite the process")
					.status(RequestStatus.PENDING)
					.student(userModelRepository.findById(5L).orElseThrow())
					.registrar(userModelRepository.findById(2L).orElseThrow())
					.build()
			));

			

				paymentRepository.save(
					Payment.builder()
					.amount(100.0)
					.paymentMethod(PaymentMethod.CASH)
					.documentrequest(documentRequestRepository.findById(1L).orElseThrow())
					.build()
				);

		};
	}

	// branch rule test 
	// test 2
}
