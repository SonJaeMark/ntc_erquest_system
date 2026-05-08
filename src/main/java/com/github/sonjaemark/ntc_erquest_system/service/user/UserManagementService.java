package com.github.sonjaemark.ntc_erquest_system.service.user;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.github.sonjaemark.ntc_erquest_system.dto.LoadAllUserResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.RegisterRequestDTO;
import com.github.sonjaemark.ntc_erquest_system.dto.RegisterResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.model.UserModel;
import com.github.sonjaemark.ntc_erquest_system.model.enums.UserRole;
import com.github.sonjaemark.ntc_erquest_system.repository.UserModelRepository;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthLevel;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthService;

@Service
public class UserManagementService extends AuthLevel{
    private final UserModelRepository userModelRepository;
    private final PasswordEncoder passwordEncoder;

    public UserManagementService(UserModelRepository userModelRepository, AuthService authService, PasswordEncoder passwordEncoder){
        super(authService);
        this.userModelRepository = userModelRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public RegisterResponseDTO register(RegisterRequestDTO registerRequestDTO){

        UserModel newUser = UserModel.builder()
                .email(registerRequestDTO.email())
                .password(passwordEncoder.encode(registerRequestDTO.password()))
                .firstName(registerRequestDTO.firstName())
                .lastName(registerRequestDTO.lastName())
                .role(registerRequestDTO.role())
                .build();

        newUser = userModelRepository.save(newUser);
        
        return new RegisterResponseDTO(newUser.getId(), newUser.getEmail(), newUser.getRole());
    }

    public boolean toggleUserActiveStatus(Long userId){
        isAuthorized(List.of(UserRole.ADMIN));
        UserModel user = userModelRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.isActive()) {
            user.setActive(false);
        } else {
            user.setActive(true);
        }
        userModelRepository.save(user);
        return user.isActive();
    }

    public List<LoadAllUserResponseDTO> getAllUsers(){
        isAuthorized(List.of(UserRole.ADMIN));
        return userModelRepository.findAll().stream()
                .map(user -> new LoadAllUserResponseDTO(
                    user.getId(), user.getEmail(), user.getRole(), user.getFirstName() + " " + user.getLastName(), user.isActive()))
                .toList();
    }
}
