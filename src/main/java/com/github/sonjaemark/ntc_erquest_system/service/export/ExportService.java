package com.github.sonjaemark.ntc_erquest_system.service.export;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.github.sonjaemark.ntc_erquest_system.model.DocumentRequest;
import com.github.sonjaemark.ntc_erquest_system.model.Payment;
import com.github.sonjaemark.ntc_erquest_system.model.enums.UserRole;
import com.github.sonjaemark.ntc_erquest_system.repository.DocumentRequestRepository;
import com.github.sonjaemark.ntc_erquest_system.repository.PaymentRepository;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthLevel;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthService;

@Service
public class ExportService extends AuthLevel {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final List<UserRole> REGISTRAR_ONLY = List.of(UserRole.REGISTRAR);

    private final DocumentRequestRepository documentRequestRepository;
    private final PaymentRepository paymentRepository;

    public ExportService(AuthService authService,
                         DocumentRequestRepository documentRequestRepository,
                         PaymentRepository paymentRepository) {
        super(authService);
        this.documentRequestRepository = documentRequestRepository;
        this.paymentRepository         = paymentRepository;
    }

    public byte[] exportDocumentRequestsToCsv() {
        isAuthorized(REGISTRAR_ONLY);

        StringBuilder sb = new StringBuilder("ID,Student ID,Student Name,Document Type,Purpose,Status,Requested At,Updated At,Remarks\n");

        for (DocumentRequest dr : documentRequestRepository.findAll()) {
            String name = dr.getStudent() != null
                ? dr.getStudent().getFirstName() + " " + dr.getStudent().getLastName() : "";
            Long sid = dr.getStudent() != null ? dr.getStudent().getId() : null;

            row(sb,
                dr.getId(), sid, name,
                dr.getDocumentType(), dr.getPurpose(), dr.getStatus(),
                fmt(dr.getRequestedAt()), fmt(dr.getUpdatedAt()), dr.getRemarks()
            );
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    public byte[] exportPaymentsToCsv() {
        isAuthorized(REGISTRAR_ONLY);

        StringBuilder sb = new StringBuilder("Payment ID,Document Request ID,Amount,Payment Method,Reference Number,Validated,Paid At\n");

        for (Payment p : paymentRepository.findAll()) {
            Long docReqId = p.getDocumentrequest() != null ? p.getDocumentrequest().getId() : null;
            row(sb,
                p.getId(), docReqId, p.getAmount(),
                p.getPaymentMethod(), p.getReferenceNumber(), p.getValidated(),
                fmt(p.getPaidAt())
            );
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private void row(StringBuilder sb, Object... values) {
        for (int i = 0; i < values.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(cell(values[i]));
        }
        sb.append("\n");
    }
    
    private String cell(Object value) {
        String s = value != null ? value.toString() : "";
        return "\"" + s.replace("\"", "\"\"") + "\"";
    }

    private String fmt(LocalDateTime dt) {
        return dt != null ? dt.format(FMT) : "";
    }
}
