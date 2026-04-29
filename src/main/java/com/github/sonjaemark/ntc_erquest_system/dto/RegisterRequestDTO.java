package com.github.sonjaemark.ntc_erquest_system.dto;


import com.github.sonjaemark.ntc_erquest_system.model.enums.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid email address")
    String email,

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    @Pattern( regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\" \\\\|,.<>\\/?])(?=.*\\d).+$",
    message = "Password must contain at least 1 uppercase letter, 1 special character, and 1 number")
    String password,

    @NotBlank(message = "Confirm password is required")
    String confirmPassword,

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    String firstName,

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    String lastName,
    
    @NotNull(message = "Role is required")
    UserRole role
) {

}
