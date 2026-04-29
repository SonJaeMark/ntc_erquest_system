package com.github.sonjaemark.ntc_erquest_system.service.user;

import java.util.List;

import org.springframework.stereotype.Service;

import com.github.sonjaemark.ntc_erquest_system.dto.RegisterResponseDTO;
import com.github.sonjaemark.ntc_erquest_system.model.UserModel;
import com.github.sonjaemark.ntc_erquest_system.model.enums.UserRole;
import com.github.sonjaemark.ntc_erquest_system.repository.UserModelRepository;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthLevel;
import com.github.sonjaemark.ntc_erquest_system.service.auth.AuthService;

@Service
public class UserManagementService extends AuthLevel{
    private final UserModelRepository userModelRepository;

    public UserManagementService(UserModelRepository userModelRepository, AuthService authService){
        super(authService);
        this.userModelRepository = userModelRepository;
    }

    public RegisterResponseDTO toRegisterResponseDTO(UserModel userModel){
        return new RegisterResponseDTO(
            userModel.getId(),
            userModel.getEmail(),
            userModel.getRole()
        );
    }

    public RegisterResponseDTO toggleActiveStatus(Long userId){
        isAuthorized(List.of(UserRole.ADMIN));
        UserModel user = userModelRepository.findById(userId).orElseThrow();

        if(user.isActive()) {
            user.setActive(false);
        } else {
            user.setActive(true);
        }

        UserModel toggledActiveStatus = userModelRepository.save(user);
        return toRegisterResponseDTO(toggledActiveStatus);
    }

    public List<RegisterResponseDTO> getAllUser(){
        isAuthorized(List.of(UserRole.ADMIN));
        return userModelRepository
            .findAll()
            .stream()
            .map(this::toRegisterResponseDTO)
            .toList();
    }
}
