package com.github.sonjaemark.ntc_erquest_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.sonjaemark.ntc_erquest_system.model.UserModel;
import com.github.sonjaemark.ntc_erquest_system.model.enums.UserRole;

public interface UserModelRepository extends JpaRepository<UserModel, Long>{

    Optional<UserModel> findByEmail(String email);
    List<UserModel> findAllByRole(UserRole role);
}
