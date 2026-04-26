package com.github.sonjaemark.ntc_erquest_system.service.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.github.sonjaemark.ntc_erquest_system.exception.EmailNotFoundException;
import com.github.sonjaemark.ntc_erquest_system.model.UserModel;
import com.github.sonjaemark.ntc_erquest_system.repository.UserModelRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserModelRepository userModelRepository;

    
    public CustomUserDetailsService(UserModelRepository userModelRepository) {
        this.userModelRepository = userModelRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws EmailNotFoundException {

        UserModel user =
            userModelRepository.findByEmail(email)
        .orElseThrow(() -> new EmailNotFoundException("User with email " + email + " not found"));

        return org.springframework.security.core.userdetails.User
            .builder()
            .username(user.getEmail())
            .password(user.getPassword())
            .roles(user.getRole().name())
        .build();
    }
}
