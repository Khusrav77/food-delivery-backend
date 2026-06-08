package com.shh.foodeliverybackendapp.modules.user.service;

import com.shh.foodeliverybackendapp.modules.user.dto.UpdateProfileRequest;
import com.shh.foodeliverybackendapp.modules.user.dto.UserResponse;
import com.shh.foodeliverybackendapp.modules.user.entity.User;

import java.util.UUID;

public interface UserService {
    UserResponse getCurrentUser();

    UserResponse findById(UUID id);

    UserResponse updateProfile(
            UpdateProfileRequest request
    );

    User findByPhone(String phone);

    boolean existsByEmail(String email);
}
