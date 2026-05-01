package com.github.sonjaemark.ntc_erquest_system.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.github.sonjaemark.ntc_erquest_system.dto.ExceptionResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleNotFound(EmailNotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                new ExceptionResponseDTO(
                    HttpStatus.NOT_FOUND, 
                    ex.getMessage(), 
                    LocalDateTime.now()
                )
            );
    }

    @ExceptionHandler(AccessTokenBlockedException.class)
    public ResponseEntity<ExceptionResponseDTO> handleAccessTokenBlocked(AccessTokenBlockedException ex) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(
                new ExceptionResponseDTO(
                    HttpStatus.UNAUTHORIZED,
                    ex.getMessage(),
                    LocalDateTime.now()
                )
            );
    }

    @ExceptionHandler(IdNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleIdNotFound(IdNotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                new ExceptionResponseDTO(
                    HttpStatus.NOT_FOUND, 
                    ex.getMessage(), 
                    LocalDateTime.now()
                )
            );  
    }

    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<ExceptionResponseDTO> handleUnauthorized(UnauthorizedUserException ex) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .body(
                new ExceptionResponseDTO(
                    HttpStatus.UNAUTHORIZED, 
                    ex.getMessage(), 
                    LocalDateTime.now()
                )
            );
    }

    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<ExceptionResponseDTO> handlePasswordInvalid(PasswordInvalidException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                new ExceptionResponseDTO(
                    HttpStatus.BAD_REQUEST, 
                    ex.getMessage(), 
                    LocalDateTime.now()
                )
            );
    }

    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<ExceptionResponseDTO> handleConflict(EmailAlreadyExistException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(
                new ExceptionResponseDTO(
                    HttpStatus.CONFLICT, 
                    ex.getMessage(), 
                    LocalDateTime.now()
                )
            );
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ExceptionResponseDTO> handleInvalidRefreshToken(InvalidRefreshTokenException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                new ExceptionResponseDTO(
                    HttpStatus.BAD_REQUEST, 
                    ex.getMessage(), 
                    LocalDateTime.now()
                )
            );
    }

    // DocumentNotFoundException
    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> handleDocumentNotFound(DocumentNotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                new ExceptionResponseDTO(
                    HttpStatus.NOT_FOUND, 
                    ex.getMessage(), 
                    LocalDateTime.now()
                )
            );
    }

    // DTO validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleBodyValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors()
            .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

        return ResponseEntity.badRequest().body(errors);
    }
    @ExceptionHandler(DocumentRequestAlreadyExistException.class)
    public ResponseEntity<ExceptionResponseDTO> handleDocumentRequestAlreadyExist(
        DocumentRequestAlreadyExistException ex
    ) {
    return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                    new ExceptionResponseDTO(
                            HttpStatus.BAD_REQUEST,
                            ex.getMessage(),
                            LocalDateTime.now()
                    )
            );
    }
}
