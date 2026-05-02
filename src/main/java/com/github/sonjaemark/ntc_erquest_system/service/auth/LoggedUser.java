package com.github.sonjaemark.ntc_erquest_system.service.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.github.sonjaemark.ntc_erquest_system.exception.UnauthorizedUserException;
import com.github.sonjaemark.ntc_erquest_system.model.UserModel;
import com.github.sonjaemark.ntc_erquest_system.repository.UserModelRepository;

@Component
public class LoggedUser {

    private final UserModelRepository userModelRepository;

    public LoggedUser(UserModelRepository userModelRepository){
        this.userModelRepository = userModelRepository;
    }

    public UserModel getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UnauthorizedUserException("User not authenticated");
        }

        Object principal = authentication.getPrincipal();
        String email;

        if (principal instanceof UserDetails userDetails) {
            email = userDetails.getUsername();
        } else if (principal instanceof String principalName) {
            email = principalName;
        } else {
            throw new UnauthorizedUserException("Unsupported authentication principal");
        }

        // Get the currently authenticated user's email from the security context
        return userModelRepository.findByEmail(email)
            .orElseThrow(() -> new UnauthorizedUserException("User not found"));
    }
}
