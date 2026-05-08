package com.github.sonjaemark.ntc_erquest_system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

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
		    RequestLogsRepository requestLogsRepository,
			PasswordEncoder passwordEncoder) {
		return args -> {
			if (userModelRepository.findByEmail("424001001@ntc.edu.ph").isEmpty()) {

				// ==================== ADMIN ====================
				userModelRepository.save(UserModel.builder()
						.firstName("Admin")
						.lastName("Admin1")
						.email("admin@ntc.edu.ph")
						.password(passwordEncoder.encode("password123"))
						.role(UserRole.ADMIN)
						.build());

				// ==================== REGISTRARS ====================
				for (int i = 1; i <= 7; i++) {
					userModelRepository.save(UserModel.builder()
							.firstName("Registrar")
							.lastName(String.valueOf(i))
							.email("registrar" + i + "@ntc.edu.ph")
							.password(passwordEncoder.encode("password123"))
							.role(UserRole.REGISTRAR)
							.build());
				}

				// ==================== STUDENTS ====================

				String[][] students = {
						{"Juan", "Dela Cruz"},
						{"Maria", "Santos"},
						{"Carlo", "Reyes"},
						{"Ana", "Villanueva"},
						{"Jose", "Mendoza"},
						{"Liza", "Cruz"},
						{"Ramon", "Garcia"},
						{"Claire", "Bautista"},
						{"Mark", "Flores"},
						{"Grace", "Navarro"},
						{"Patrick", "Lim"},
						{"Sophia", "Ramos"},
						{"Leo", "Torres"},
						{"Nina", "Castillo"},
						{"Victor", "Aquino"},
						{"Camille", "Reyes"},
						{"Daniel", "Ong"},
						{"Isabel", "Tan"},
						{"Kevin", "Morales"},
						{"Bianca", "Luna"}
				};

				for (int i = 0; i < students.length; i++) {

					int studentNumber = 424001001 + i;

					UserModel student = UserModel.builder()
							.firstName(students[i][0])
							.lastName(students[i][1])
							.email(studentNumber + "@ntc.edu.ph")
							.password(passwordEncoder.encode("password123"))
							.role(UserRole.STUDENT)
							.build();

					userModelRepository.save(student);
				}
			}

			List<UserModel> students = userModelRepository.findAllByRole(UserRole.STUDENT);
			if(students.isEmpty()){
				throw new IllegalArgumentException("No students found");
			}
			// add 3 documents for each student
			for (UserModel student : students) {
				for (DocumentType documentType : DocumentType.values()) {
					documentRepository.save(Document.builder()
							.documentType(documentType)
							.student(student)
							.documentContent("Document content for " + documentType  + " url: https://www.ntc.edu.ph" + student.getEmail())
							.build());
				}
			}
		};
	}
}
