package com.github.sonjaemark.ntc_erquest_system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.github.sonjaemark.ntc_erquest_system.model.Document;
import com.github.sonjaemark.ntc_erquest_system.model.DocumentRequest;
import com.github.sonjaemark.ntc_erquest_system.model.UserModel;
import com.github.sonjaemark.ntc_erquest_system.model.enums.DocumentType;
import com.github.sonjaemark.ntc_erquest_system.model.enums.Purpose;
import com.github.sonjaemark.ntc_erquest_system.model.enums.RequestStatus;
import com.github.sonjaemark.ntc_erquest_system.model.enums.UserRole;
import com.github.sonjaemark.ntc_erquest_system.repository.DocumentRepository;
import com.github.sonjaemark.ntc_erquest_system.repository.DocumentRequestRepository;
import com.github.sonjaemark.ntc_erquest_system.repository.UserModelRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UserModelRepository userModelRepository;
    @Autowired private DocumentRepository documentRepository;
    @Autowired private DocumentRequestRepository documentRequestRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userModelRepository.findByEmail("42400001@test.com").isEmpty()) {

            // ==================== ADMIN ====================
            userModelRepository.save(UserModel.builder()
                    .firstName("Admin")
                    .lastName("admin1")
                    .email("admin@test.com")
                    .password(passwordEncoder.encode("password"))
                    .role(UserRole.ADMIN)
                    .build());

            // ==================== STUDENT 1 - Juan Dela Cruz ====================
            UserModel s1 = userModelRepository.save(UserModel.builder()
                    .firstName("Juan").lastName("Dela Cruz")
                    .email("42400001@test.com")
                    .password(passwordEncoder.encode("password"))
                    .role(UserRole.STUDENT).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s1)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.TOR).documentContent("TOR - Juan").student(s1).build()))
                    .documentType(DocumentType.TOR).purpose(Purpose.SCHOLARSHIP)
                    .additionalDetails("For scholarship").build());

            documentRequestRepository.save(DocumentRequest.builder().student(s1)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.GOOD_MORAL).documentContent("Good Moral - Juan").student(s1).build()))
                    .documentType(DocumentType.GOOD_MORAL).purpose(Purpose.WORK)
                    .additionalDetails("For job application").status(RequestStatus.RELEASED).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s1)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.DIPLOMA).documentContent("Diploma - Juan").student(s1).build()))
                    .documentType(DocumentType.DIPLOMA).purpose(Purpose.TRANSFERRING_SCHOOL)
                    .additionalDetails("Transferring school").status(RequestStatus.CANCELLED)
                    .remarks("No longer needed").build());

            // ==================== STUDENT 2 - Maria Santos ====================
            UserModel s2 = userModelRepository.save(UserModel.builder()
                    .firstName("Maria").lastName("Santos")
                    .email("42400002@test.com")
                    .password(passwordEncoder.encode("password"))
                    .role(UserRole.STUDENT).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s2)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.CERTIFICATE_OF_ENROLLMENT).documentContent("Enrollment Cert - Maria").student(s2).build()))
                    .documentType(DocumentType.CERTIFICATE_OF_ENROLLMENT).purpose(Purpose.SCHOLARSHIP)
                    .additionalDetails("Scholarship requirement").status(RequestStatus.PROCESSING).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s2)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.TOR).documentContent("TOR - Maria").student(s2).build()))
                    .documentType(DocumentType.TOR).purpose(Purpose.OJT)
                    .additionalDetails("OJT requirement").status(RequestStatus.REJECTED)
                    .remarks("Incomplete requirements").build());

            // ==================== STUDENT 3 - Carlo Reyes ====================
            UserModel s3 = userModelRepository.save(UserModel.builder()
                    .firstName("Carlo").lastName("Reyes")
                    .email("42400003@test.com")
                    .password(passwordEncoder.encode("password"))
                    .role(UserRole.STUDENT).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s3)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.GOOD_MORAL).documentContent("Good Moral - Carlo").student(s3).build()))
                    .documentType(DocumentType.GOOD_MORAL).purpose(Purpose.WORK)
                    .additionalDetails("Job requirement").status(RequestStatus.READY_FOR_RELEASE).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s3)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.DIPLOMA).documentContent("Diploma - Carlo").student(s3).build()))
                    .documentType(DocumentType.DIPLOMA).purpose(Purpose.TRANSFERRING_SCHOOL)
                    .additionalDetails("Transfer docs").status(RequestStatus.RELEASED).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s3)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.TOR).documentContent("TOR - Carlo").student(s3).build()))
                    .documentType(DocumentType.TOR).purpose(Purpose.SCHOLARSHIP)
                    .additionalDetails("Scholarship docs").build());

            // ==================== STUDENT 4 - Ana Villanueva ====================
            UserModel s4 = userModelRepository.save(UserModel.builder()
                    .firstName("Ana").lastName("Villanueva")
                    .email("42400004@test.com")
                    .password(passwordEncoder.encode("password"))
                    .role(UserRole.STUDENT).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s4)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.CERTIFICATE_OF_REGISTRATION).documentContent("COR - Ana").student(s4).build()))
                    .documentType(DocumentType.CERTIFICATE_OF_REGISTRATION).purpose(Purpose.SCHOLARSHIP)
                    .additionalDetails("For CHED scholarship").status(RequestStatus.PROCESSING).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s4)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.HONORABLE_DISMISSAL).documentContent("Honorable Dismissal - Ana").student(s4).build()))
                    .documentType(DocumentType.HONORABLE_DISMISSAL).purpose(Purpose.TRANSFERRING_SCHOOL)
                    .additionalDetails("Transferring to UP").status(RequestStatus.CANCELLED)
                    .remarks("Changed mind").build());

            // ==================== STUDENT 5 - Jose Mendoza ====================
            UserModel s5 = userModelRepository.save(UserModel.builder()
                    .firstName("Jose").lastName("Mendoza")
                    .email("42400005@test.com")
                    .password(passwordEncoder.encode("password"))
                    .role(UserRole.STUDENT).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s5)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.TOR).documentContent("TOR - Jose").student(s5).build()))
                    .documentType(DocumentType.TOR).purpose(Purpose.OJT)
                    .additionalDetails("OJT company requirement").status(RequestStatus.RELEASED).build());

            // ==================== STUDENT 6 - Liza Cruz ====================
            UserModel s6 = userModelRepository.save(UserModel.builder()
                    .firstName("Liza").lastName("Cruz")
                    .email("42400006@test.com")
                    .password(passwordEncoder.encode("password"))
                    .role(UserRole.STUDENT).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s6)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.GOOD_MORAL).documentContent("Good Moral - Liza").student(s6).build()))
                    .documentType(DocumentType.GOOD_MORAL).purpose(Purpose.WORK)
                    .additionalDetails("Job application").build());

            documentRequestRepository.save(DocumentRequest.builder().student(s6)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.DIPLOMA).documentContent("Diploma - Liza").student(s6).build()))
                    .documentType(DocumentType.DIPLOMA).purpose(Purpose.WORK)
                    .additionalDetails("Required by employer").status(RequestStatus.READY_FOR_RELEASE).build());

            // ==================== STUDENT 7 - Ramon Garcia ====================
            UserModel s7 = userModelRepository.save(UserModel.builder()
                    .firstName("Ramon").lastName("Garcia")
                    .email("42400007@test.com")
                    .password(passwordEncoder.encode("password"))
                    .role(UserRole.STUDENT).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s7)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.CERTIFICATE_OF_ENROLLMENT).documentContent("Enrollment - Ramon").student(s7).build()))
                    .documentType(DocumentType.CERTIFICATE_OF_ENROLLMENT).purpose(Purpose.SCHOLARSHIP)
                    .additionalDetails("DOST scholarship").status(RequestStatus.REJECTED)
                    .remarks("Wrong document submitted").build());

            documentRequestRepository.save(DocumentRequest.builder().student(s7)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.TOR).documentContent("TOR - Ramon").student(s7).build()))
                    .documentType(DocumentType.TOR).purpose(Purpose.SCHOLARSHIP)
                    .additionalDetails("Resubmission").build());

            // ==================== STUDENT 8 - Claire Bautista ====================
            UserModel s8 = userModelRepository.save(UserModel.builder()
                    .firstName("Claire").lastName("Bautista")
                    .email("42400008@test.com")
                    .password(passwordEncoder.encode("password"))
                    .role(UserRole.STUDENT).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s8)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.HONORABLE_DISMISSAL).documentContent("HD - Claire").student(s8).build()))
                    .documentType(DocumentType.HONORABLE_DISMISSAL).purpose(Purpose.TRANSFERRING_SCHOOL)
                    .additionalDetails("Transfer to Mapua").status(RequestStatus.PROCESSING).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s8)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.CERTIFICATE_OF_REGISTRATION).documentContent("COR - Claire").student(s8).build()))
                    .documentType(DocumentType.CERTIFICATE_OF_REGISTRATION).purpose(Purpose.SCHOLARSHIP)
                    .additionalDetails("Private scholarship").status(RequestStatus.RELEASED).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s8)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.GOOD_MORAL).documentContent("Good Moral - Claire").student(s8).build()))
                    .documentType(DocumentType.GOOD_MORAL).purpose(Purpose.WORK)
                    .additionalDetails("BPO application").status(RequestStatus.CANCELLED)
                    .remarks("Cancelled by student").build());

            // ==================== STUDENT 9 - Mark Flores ====================
            UserModel s9 = userModelRepository.save(UserModel.builder()
                    .firstName("Mark").lastName("Flores")
                    .email("42400009@test.com")
                    .password(passwordEncoder.encode("password"))
                    .role(UserRole.STUDENT).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s9)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.TOR).documentContent("TOR - Mark").student(s9).build()))
                    .documentType(DocumentType.TOR).purpose(Purpose.OJT)
                    .additionalDetails("OJT requirement").status(RequestStatus.READY_FOR_RELEASE).build());

            // ==================== STUDENT 10 - Grace Navarro ====================
            UserModel s10 = userModelRepository.save(UserModel.builder()
                    .firstName("Grace").lastName("Navarro")
                    .email("42400010@test.com")
                    .password(passwordEncoder.encode("password"))
                    .role(UserRole.STUDENT).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s10)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.DIPLOMA).documentContent("Diploma - Grace").student(s10).build()))
                    .documentType(DocumentType.DIPLOMA).purpose(Purpose.WORK)
                    .additionalDetails("Employment requirement").status(RequestStatus.RELEASED).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s10)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.GOOD_MORAL).documentContent("Good Moral - Grace").student(s10).build()))
                    .documentType(DocumentType.GOOD_MORAL).purpose(Purpose.WORK)
                    .additionalDetails("Second company").build());

            // ==================== STUDENT 11 - Patrick Lim ====================
            UserModel s11 = userModelRepository.save(UserModel.builder()
                    .firstName("Patrick").lastName("Lim")
                    .email("42400011@test.com")
                    .password(passwordEncoder.encode("password"))
                    .role(UserRole.STUDENT).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s11)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.CERTIFICATE_OF_ENROLLMENT).documentContent("Enrollment - Patrick").student(s11).build()))
                    .documentType(DocumentType.CERTIFICATE_OF_ENROLLMENT).purpose(Purpose.SCHOLARSHIP)
                    .additionalDetails("Scholarship renewal").status(RequestStatus.PROCESSING).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s11)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.TOR).documentContent("TOR - Patrick").student(s11).build()))
                    .documentType(DocumentType.TOR).purpose(Purpose.OJT)
                    .additionalDetails("OJT host company").status(RequestStatus.REJECTED)
                    .remarks("Grades not updated").build());

            documentRequestRepository.save(DocumentRequest.builder().student(s11)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.GOOD_MORAL).documentContent("Good Moral - Patrick").student(s11).build()))
                    .documentType(DocumentType.GOOD_MORAL).purpose(Purpose.OJT)
                    .additionalDetails("OJT resubmission").build());

            // ==================== STUDENT 12 - Sophia Ramos ====================
            UserModel s12 = userModelRepository.save(UserModel.builder()
                    .firstName("Sophia").lastName("Ramos")
                    .email("42400012@test.com")
                    .password(passwordEncoder.encode("password"))
                    .role(UserRole.STUDENT).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s12)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.HONORABLE_DISMISSAL).documentContent("HD - Sophia").student(s12).build()))
                    .documentType(DocumentType.HONORABLE_DISMISSAL).purpose(Purpose.TRANSFERRING_SCHOOL)
                    .additionalDetails("Transfer to Ateneo").status(RequestStatus.READY_FOR_RELEASE).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s12)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.CERTIFICATE_OF_REGISTRATION).documentContent("COR - Sophia").student(s12).build()))
                    .documentType(DocumentType.CERTIFICATE_OF_REGISTRATION).purpose(Purpose.SCHOLARSHIP)
                    .additionalDetails("Full scholarship").status(RequestStatus.CANCELLED)
                    .remarks("Already enrolled elsewhere").build());

            // ==================== STUDENT 13 - Leo Torres ====================
            UserModel s13 = userModelRepository.save(UserModel.builder()
                    .firstName("Leo").lastName("Torres")
                    .email("42400013@test.com")
                    .password(passwordEncoder.encode("password"))
                    .role(UserRole.STUDENT).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s13)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.TOR).documentContent("TOR - Leo").student(s13).build()))
                    .documentType(DocumentType.TOR).purpose(Purpose.WORK)
                    .additionalDetails("Work abroad requirement").status(RequestStatus.RELEASED).build());

            // ==================== STUDENT 14 - Nina Castillo ====================
            UserModel s14 = userModelRepository.save(UserModel.builder()
                    .firstName("Nina").lastName("Castillo")
                    .email("42400014@test.com")
                    .password(passwordEncoder.encode("password"))
                    .role(UserRole.STUDENT).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s14)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.GOOD_MORAL).documentContent("Good Moral - Nina").student(s14).build()))
                    .documentType(DocumentType.GOOD_MORAL).purpose(Purpose.SCHOLARSHIP)
                    .additionalDetails("Private scholarship").build());

            documentRequestRepository.save(DocumentRequest.builder().student(s14)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.DIPLOMA).documentContent("Diploma - Nina").student(s14).build()))
                    .documentType(DocumentType.DIPLOMA).purpose(Purpose.WORK)
                    .additionalDetails("Job application abroad").status(RequestStatus.PROCESSING).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s14)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.TOR).documentContent("TOR - Nina").student(s14).build()))
                    .documentType(DocumentType.TOR).purpose(Purpose.OJT)
                    .additionalDetails("OJT host requirement").status(RequestStatus.REJECTED)
                    .remarks("Missing signature").build());

            // ==================== STUDENT 15 - Victor Aquino ====================
            UserModel s15 = userModelRepository.save(UserModel.builder()
                    .firstName("Victor").lastName("Aquino")
                    .email("42400015@test.com")
                    .password(passwordEncoder.encode("password"))
                    .role(UserRole.STUDENT).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s15)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.CERTIFICATE_OF_ENROLLMENT).documentContent("Enrollment - Victor").student(s15).build()))
                    .documentType(DocumentType.CERTIFICATE_OF_ENROLLMENT).purpose(Purpose.SCHOLARSHIP)
                    .additionalDetails("Government scholarship").status(RequestStatus.READY_FOR_RELEASE).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s15)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.HONORABLE_DISMISSAL).documentContent("HD - Victor").student(s15).build()))
                    .documentType(DocumentType.HONORABLE_DISMISSAL).purpose(Purpose.TRANSFERRING_SCHOOL)
                    .additionalDetails("Transfer to DLSU").status(RequestStatus.RELEASED).build());

            // ==================== STUDENT 16 - Camille Reyes ====================
            UserModel s16 = userModelRepository.save(UserModel.builder()
                    .firstName("Camille").lastName("Reyes")
                    .email("42400016@test.com")
                    .password(passwordEncoder.encode("password"))
                    .role(UserRole.STUDENT).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s16)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.TOR).documentContent("TOR - Camille").student(s16).build()))
                    .documentType(DocumentType.TOR).purpose(Purpose.WORK)
                    .additionalDetails("Local employment").build());

            documentRequestRepository.save(DocumentRequest.builder().student(s16)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.GOOD_MORAL).documentContent("Good Moral - Camille").student(s16).build()))
                    .documentType(DocumentType.GOOD_MORAL).purpose(Purpose.OJT)
                    .additionalDetails("OJT requirement").status(RequestStatus.CANCELLED)
                    .remarks("OJT cancelled").build());

            // ==================== STUDENT 17 - Daniel Ong ====================
            UserModel s17 = userModelRepository.save(UserModel.builder()
                    .firstName("Daniel").lastName("Ong")
                    .email("42400017@test.com")
                    .password(passwordEncoder.encode("password"))
                    .role(UserRole.STUDENT).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s17)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.DIPLOMA).documentContent("Diploma - Daniel").student(s17).build()))
                    .documentType(DocumentType.DIPLOMA).purpose(Purpose.WORK)
                    .additionalDetails("Employment abroad").status(RequestStatus.PROCESSING).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s17)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.CERTIFICATE_OF_REGISTRATION).documentContent("COR - Daniel").student(s17).build()))
                    .documentType(DocumentType.CERTIFICATE_OF_REGISTRATION).purpose(Purpose.SCHOLARSHIP)
                    .additionalDetails("Tuition scholarship").status(RequestStatus.RELEASED).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s17)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.TOR).documentContent("TOR - Daniel").student(s17).build()))
                    .documentType(DocumentType.TOR).purpose(Purpose.OJT)
                    .additionalDetails("OJT host company").build());

            // ==================== STUDENT 18 - Isabel Tan ====================
            UserModel s18 = userModelRepository.save(UserModel.builder()
                    .firstName("Isabel").lastName("Tan")
                    .email("42400018@test.com")
                    .password(passwordEncoder.encode("password"))
                    .role(UserRole.STUDENT).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s18)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.GOOD_MORAL).documentContent("Good Moral - Isabel").student(s18).build()))
                    .documentType(DocumentType.GOOD_MORAL).purpose(Purpose.SCHOLARSHIP)
                    .additionalDetails("Merit scholarship").status(RequestStatus.REJECTED)
                    .remarks("Document expired").build());

            documentRequestRepository.save(DocumentRequest.builder().student(s18)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.CERTIFICATE_OF_ENROLLMENT).documentContent("Enrollment - Isabel").student(s18).build()))
                    .documentType(DocumentType.CERTIFICATE_OF_ENROLLMENT).purpose(Purpose.SCHOLARSHIP)
                    .additionalDetails("Reapplication").status(RequestStatus.READY_FOR_RELEASE).build());

            // ==================== STUDENT 19 - Kevin Morales ====================
            UserModel s19 = userModelRepository.save(UserModel.builder()
                    .firstName("Kevin").lastName("Morales")
                    .email("42400019@test.com")
                    .password(passwordEncoder.encode("password"))
                    .role(UserRole.STUDENT).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s19)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.HONORABLE_DISMISSAL).documentContent("HD - Kevin").student(s19).build()))
                    .documentType(DocumentType.HONORABLE_DISMISSAL).purpose(Purpose.TRANSFERRING_SCHOOL)
                    .additionalDetails("Transfer to UST").status(RequestStatus.RELEASED).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s19)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.TOR).documentContent("TOR - Kevin").student(s19).build()))
                    .documentType(DocumentType.TOR).purpose(Purpose.WORK)
                    .additionalDetails("Job requirement").build());

            // ==================== STUDENT 20 - Bianca Luna ====================
            UserModel s20 = userModelRepository.save(UserModel.builder()
                    .firstName("Bianca").lastName("Luna")
                    .email("42400020@test.com")
                    .password(passwordEncoder.encode("password"))
                    .role(UserRole.STUDENT).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s20)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.CERTIFICATE_OF_REGISTRATION).documentContent("COR - Bianca").student(s20).build()))
                    .documentType(DocumentType.CERTIFICATE_OF_REGISTRATION).purpose(Purpose.SCHOLARSHIP)
                    .additionalDetails("Scholarship application").status(RequestStatus.PROCESSING).build());

            documentRequestRepository.save(DocumentRequest.builder().student(s20)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.DIPLOMA).documentContent("Diploma - Bianca").student(s20).build()))
                    .documentType(DocumentType.DIPLOMA).purpose(Purpose.WORK)
                    .additionalDetails("Abroad application").status(RequestStatus.CANCELLED)
                    .remarks("Plans changed").build());

            documentRequestRepository.save(DocumentRequest.builder().student(s20)
                    .document(documentRepository.save(Document.builder().documentType(DocumentType.GOOD_MORAL).documentContent("Good Moral - Bianca").student(s20).build()))
                    .documentType(DocumentType.GOOD_MORAL).purpose(Purpose.WORK)
                    .additionalDetails("Local job").status(RequestStatus.READY_FOR_RELEASE).build());
        }
    }
}