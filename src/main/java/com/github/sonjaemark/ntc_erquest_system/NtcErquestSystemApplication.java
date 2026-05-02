package com.github.sonjaemark.ntc_erquest_system;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.github.sonjaemark.ntc_erquest_system.dto.RegisterRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.model.Document;
import com.github.sonjaemark.ntc_erquest_system.model.DocumentRequest;
import com.github.sonjaemark.ntc_erquest_system.model.RequestLogs;
import com.github.sonjaemark.ntc_erquest_system.model.UserModel;
import com.github.sonjaemark.ntc_erquest_system.model.enums.DocumentType;
import com.github.sonjaemark.ntc_erquest_system.model.enums.Purpose;
import com.github.sonjaemark.ntc_erquest_system.model.enums.RequestStatus;
import com.github.sonjaemark.ntc_erquest_system.model.enums.UserRole;
import com.github.sonjaemark.ntc_erquest_system.repository.DocumentRepository;
import com.github.sonjaemark.ntc_erquest_system.repository.DocumentRequestRepository;
import com.github.sonjaemark.ntc_erquest_system.repository.PaymentRepository;
import com.github.sonjaemark.ntc_erquest_system.repository.RequestLogsRepository;
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
		    PaymentRepository paymentRepository,
		    RequestLogsRepository requestLogsRepository) {
		return args -> {
			// 1. Register Accounts (Idempotent)
			List<RegisterRequestDTO> usersToRegister = List.of(
				new RegisterRequestDTO("student1@email.com", "password123", "password123", "Student", "One", UserRole.STUDENT),
				new RegisterRequestDTO("student2@email.com", "password123", "password123", "Student", "Two", UserRole.STUDENT),
				new RegisterRequestDTO("student3@email.com", "password123", "password123", "Student", "Three", UserRole.STUDENT),
				new RegisterRequestDTO("registrar1@email.com", "password123", "password123", "Registrar", "One", UserRole.REGISTRAR),
				new RegisterRequestDTO("registrar2@email.com", "password123", "password123", "Registrar", "Two", UserRole.REGISTRAR)
			);

			for (RegisterRequestDTO req : usersToRegister) {
				if (userModelRepository.findByEmail(req.email()).isEmpty()) {
					authService.register(req);
				}
			}

			UserModel s1 = userModelRepository.findByEmail("student1@email.com").orElseThrow();
			UserModel s2 = userModelRepository.findByEmail("student2@email.com").orElseThrow();
			UserModel s3 = userModelRepository.findByEmail("student3@email.com").orElseThrow();
			UserModel r1 = userModelRepository.findByEmail("registrar1@email.com").orElseThrow();
			UserModel r2 = userModelRepository.findByEmail("registrar2@email.com").orElseThrow();

			// 2. Seed Documents for Students (Check if already seeded)
			if (documentRepository.findAllByStudentId(s1.getId()).isEmpty()) {
				for (DocumentType type : DocumentType.values()) {
					documentRepository.save(Document.builder().documentType(type).student(s1).documentContent("content_" + type).build());
				}
			}
			if (documentRepository.findAllByStudentId(s2.getId()).isEmpty()) {
				List.of(DocumentType.TOR, DocumentType.GOOD_MORAL, DocumentType.CERTIFICATE_OF_REGISTRATION).forEach(type -> 
					documentRepository.save(Document.builder().documentType(type).student(s2).documentContent("content_" + type).build())
				);
			}
			if (documentRepository.findAllByStudentId(s3.getId()).isEmpty()) {
				List.of(DocumentType.DIPLOMA, DocumentType.HONORABLE_DISMISSAL, DocumentType.CERTIFICATE_OF_REGISTRATION).forEach(type -> 
					documentRepository.save(Document.builder().documentType(type).student(s3).documentContent("content_" + type).build())
				);
			}

			// 3. Seed Document Requests (Check if already seeded)
			if (documentRequestRepository.findByStudentId(s1.getId()).isEmpty() && 
			    documentRequestRepository.findByStudentId(s2.getId()).isEmpty() && 
				documentRequestRepository.findByStudentId(s3.getId()).isEmpty()) {
				
				// Helper to create request and log
				var createRequest = new Object() {
					void apply(UserModel student, UserModel registrar, DocumentType type, RequestStatus status, String details) {
						Document doc = documentRepository.findAllByStudentId(student.getId()).stream()
							.filter(d -> d.getDocumentType() == type)
							.findFirst().orElseThrow();
						
						DocumentRequest req = DocumentRequest.builder()
							.purpose(Purpose.WORK)
							.documentType(type)
							.document(doc)
							.additionalDetails(details)
							.status(status)
							.student(student)
							.registrar(registrar)
							.build();
						
						DocumentRequest savedReq = documentRequestRepository.save(req);
						
						// Initial log
						requestLogsRepository.save(RequestLogs.builder().documentRequest(savedReq).requestStatus(RequestStatus.PENDING).remarks("Request submitted").build());
						
						// If not pending, add transition logs
						if (status != RequestStatus.PENDING) {
							if (status == RequestStatus.RELEASED || status == RequestStatus.PROCESSING || status == RequestStatus.READY_FOR_RELEASE) {
								requestLogsRepository.save(RequestLogs.builder().documentRequest(savedReq).requestStatus(RequestStatus.PROCESSING).remarks("Request accepted and processing").build());
							}
							if (status == RequestStatus.RELEASED || status == RequestStatus.READY_FOR_RELEASE) {
								requestLogsRepository.save(RequestLogs.builder().documentRequest(savedReq).requestStatus(RequestStatus.READY_FOR_RELEASE).remarks("Document ready for release").build());
							}
							if (status == RequestStatus.RELEASED) {
								requestLogsRepository.save(RequestLogs.builder().documentRequest(savedReq).requestStatus(RequestStatus.RELEASED).remarks("Document has been released").build());
							}
							if (status == RequestStatus.CANCELLED) {
								requestLogsRepository.save(RequestLogs.builder().documentRequest(savedReq).requestStatus(RequestStatus.CANCELLED).remarks("Request cancelled by student").build());
							}
						}
					}
				};

				// Student 1: 1 pending, 3 released (processed by Registrar 1)
				createRequest.apply(s1, null, DocumentType.CERTIFICATE_OF_ENROLLMENT, RequestStatus.PENDING, "Pending request");
				createRequest.apply(s1, r1, DocumentType.TOR, RequestStatus.RELEASED, "Released TOR");
				createRequest.apply(s1, r1, DocumentType.GOOD_MORAL, RequestStatus.RELEASED, "Released Good Moral");
				createRequest.apply(s1, r1, DocumentType.DIPLOMA, RequestStatus.RELEASED, "Released Diploma");

				// Student 2: 1 pending, 2 released (processed by Registrar 2), 1 cancelled
				createRequest.apply(s2, null, DocumentType.TOR, RequestStatus.PENDING, "Pending TOR");
				createRequest.apply(s2, r2, DocumentType.GOOD_MORAL, RequestStatus.RELEASED, "Released Good Moral");
				createRequest.apply(s2, r2, DocumentType.CERTIFICATE_OF_REGISTRATION, RequestStatus.RELEASED, "Released COR");
				createRequest.apply(s2, null, DocumentType.TOR, RequestStatus.CANCELLED, "Cancelled request");

				// Student 3: 1 pending
				createRequest.apply(s3, null, DocumentType.DIPLOMA, RequestStatus.PENDING, "Pending Diploma");
			}
		};
	}

	// branch rule test 
	// test 2
}
