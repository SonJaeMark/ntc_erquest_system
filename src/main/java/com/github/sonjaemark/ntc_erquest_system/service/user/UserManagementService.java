package com.github.sonjaemark.ntc_erquest_system.service.user;

import com.github.sonjaemark.ntc_erquest_system.repository.UserModelRepository;

public class UserManagementService {
    private final UserModelRepository userModelRepository;

    public UserManagementService(UserModelRepository userModelRepository){
        this.userModelRepository = userModelRepository;
    }
}
